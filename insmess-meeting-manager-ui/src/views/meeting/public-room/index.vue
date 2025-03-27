<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="会议名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['meeting:meeting:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['meeting:meeting:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['meeting:meeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['meeting:meeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="会议名称" align="center" prop="name" />
      <el-table-column label="主持人" align="center" prop="hostRealname" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="开始时间" align="center" prop="startTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['meeting:meeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['meeting:meeting:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-dot-square"
            @click="joinMeeting(scope.row)"
          >加入</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-dot-square"
            @click="showRecordList(scope.row)"
          >录屏</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会议对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会议名称" prop="name">
          <el-input v-model="form.name" placeholder="会议名称" />
        </el-form-item>
        <el-form-item label="会议描述" prop="description">
          <el-input v-model="form.description" placeholder="会议描述" />
        </el-form-item>
        <el-form-item label="主持人" prop="hostUsername">
          <el-select v-model="form.hostUsername" placeholder="请选择主持人">
            <el-option v-for="user in userList" :key="user.id" :label="user.nickName" :value="user.userName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="会议成员">
          <el-select v-model="form.usernames" multiple placeholder="会议成员">
            <el-option v-for="user in userList" :key="user.id" :label="user.nickName" :value="user.userName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" type="datetime"
            :style="{width: '100%'}" placeholder="请选择日期选择" clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="会议时长" prop="duration">
          <el-input v-model="form.duration" placeholder="会议时长" />
        </el-form-item>
        <el-form-item label="会议密码" prop="password">
          <el-input v-model="form.password" placeholder="会议密码" :disabled="true"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 录屏的对话框 -->
    <el-dialog title="录屏列表" :visible.sync="openMeetingRecording" width="700px" append-to-body>
      <el-table v-loading="recordingLoading" :data="recordList">
        <el-table-column label="录屏名称" align="center" prop="recordName" />
        <el-table-column label="录屏时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="playRecording(scope.row)"
            >播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting, getWebPrepareUrl } from "@/api/meeting/meeting";
import { listRecord } from "@/api/meeting/record";
import { getToken } from '@/utils/auth'
import { listUser } from "@/api/system/user";
export default {
  name: "Meeting",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会议表格数据
      meetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "会议名称", trigger: "blur" },
          { min: 2, max: 20, message: '会议名称必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        hostUsername: [
          { required: true, message: "请选择主持人", trigger: "change" },
        ],
        startTime: [
          { required: true, message: "请选择开始时间", trigger: "change" },
        ],
      },
      userList: [],
      openMeetingRecording: false,
      recordingLoading: false,
      recordList: []
    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  methods: {
    /** 查询用户列表 */
    getUserList() {
      this.loading = true;
      listUser().then(response => {
          this.userList = response.rows;
          console.log('userList:', this.userList)
        }
      );
    },
    /** 查询会议列表 */
    getList() {
      this.loading = true;
      listMeeting(this.queryParams).then(response => {
        this.meetingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会议";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMeeting(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会议";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除会议编号为"' + ids + '"的数据项？').then(function() {
        return delMeeting(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('meeting/meeting/export', {
        ...this.queryParams
      }, `meeting_${new Date().getTime()}.xlsx`)
    },
    /** 加入会议 */
    joinMeeting(row) {
      getWebPrepareUrl().then(response => {
        let url = response.data;
        let token = getToken();
        window.open(`${url}?roomId=${row.imMeetingId}&token=${token}`, '_blank')
      });
    },
    showRecordList(row) {
      this.openMeetingRecording = true;
      this.recordingLoading = true;
      this.recordList = [];
      listRecord({
        imMeetingId: row.imMeetingId
      }).then(response => {
        this.recordList = response.rows;
        this.recordingLoading = false;
      });
    },
    playRecording(row) {
      window.open(row.recordUrl);
    }
  }
};
</script>
