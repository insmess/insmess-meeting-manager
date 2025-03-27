package com.ruoyi.web.controller.meeting;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.meeting.domain.MtMeeting;
import com.ruoyi.meeting.service.IMtMeetingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会议Controller
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
@Api("会议信息管理")
@RestController
@RequestMapping("/meeting/meeting")
public class MtMeetingController extends BaseController
{
    @Autowired
    private IMtMeetingService mtMeetingService;

    @Value("${im-meeting.web-prepare-url}")
    private String webPrepareUrl;

    /**
     * 获取会议跳转的链接
     */
    @GetMapping(value = "/getWebPrepareUrl")
    public AjaxResult getWebPrepareUrl()
    {
        //转成object类型，否则会按照String参数的重载，将结果放到message中
        Object result = webPrepareUrl;
        return success(result);
    }

    /**
     * 查询会议列表
     */
    @ApiOperation("获取会议列表")
    @PreAuthorize("@ss.hasPermi('meeting:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtMeeting mtMeeting)
    {
        startPage();
        List<MtMeeting> list = mtMeetingService.selectMtMeetingList(mtMeeting);
        return getDataTable(list);
    }

    /**
     * 导出会议列表
     */
    @PreAuthorize("@ss.hasPermi('meeting:meeting:export')")
    @Log(title = "会议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MtMeeting mtMeeting)
    {
        List<MtMeeting> list = mtMeetingService.selectMtMeetingList(mtMeeting);
        ExcelUtil<MtMeeting> util = new ExcelUtil<MtMeeting>(MtMeeting.class);
        util.exportExcel(response, list, "会议数据");
    }

    /**
     * 获取会议详细信息
     */
    @PreAuthorize("@ss.hasPermi('meeting:meeting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mtMeetingService.selectMtMeetingById(id));
    }

    /**
     * 新增会议
     */
    @ApiOperation("新增会议")
    @PreAuthorize("@ss.hasPermi('meeting:meeting:add')")
    @Log(title = "会议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MtMeeting mtMeeting)
    {
        return toAjax(mtMeetingService.insertMtMeeting(mtMeeting));
    }

    /**
     * 修改会议
     */
    @PreAuthorize("@ss.hasPermi('meeting:meeting:edit')")
    @Log(title = "会议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MtMeeting mtMeeting)
    {
        return toAjax(mtMeetingService.updateMtMeeting(mtMeeting));
    }

    /**
     * 删除会议
     */
    @PreAuthorize("@ss.hasPermi('meeting:meeting:remove')")
    @Log(title = "会议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mtMeetingService.deleteMtMeetingByIds(ids));
    }
}
