package com.ruoyi.meeting.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.meeting.sdk.ImMeetingSdk;
import com.insmess.meeting.sdk.dto.ImResultDto;
import com.insmess.meeting.sdk.dto.ImRoom;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MtMeetingMapper;
import com.ruoyi.meeting.domain.MtMeeting;
import com.ruoyi.meeting.service.IMtMeetingService;

/**
 * 会议Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-24
 */
@Service
public class MtMeetingServiceImpl implements IMtMeetingService
{
    @Autowired
    private MtMeetingMapper mtMeetingMapper;

    @Autowired
    private ImMeetingSdk imMeetingSdk;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询会议
     *
     * @param id 会议主键
     * @return 会议
     */
    @Override
    public MtMeeting selectMtMeetingById(Long id)
    {
        MtMeeting mtMeeting = mtMeetingMapper.selectMtMeetingById(id);
        //根据id查询即讯会议系统的表
        ImResultDto<ImRoom> imResultDto;
        try {
            imResultDto = imMeetingSdk.getRoomById(mtMeeting.getImMeetingId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询即讯会议失败：" + e.getMessage());
        }
        if (!imResultDto.getCode().equals(200)) {
            throw new RuntimeException("查询即讯会议失败");
        }
        ImRoom imRoom = imResultDto.getData();
        //设置值
        setProperties(mtMeeting, imRoom);
        return mtMeeting;
    }

    private void setProperties(MtMeeting mtMeeting, ImRoom imRoom) {
        mtMeeting.setName(imRoom.getName());
        mtMeeting.setDescription(imRoom.getDescription());
        mtMeeting.setHostUsername(imRoom.getHostUsername());
        mtMeeting.setHostRealname(imRoom.getHostRealname());
        mtMeeting.setCreateTime(imRoom.getCreateTime());
        mtMeeting.setStartTime(imRoom.getStartTime());
        mtMeeting.setDuration(imRoom.getDuration());
        mtMeeting.setCreateUsername(imRoom.getCreateUsername());
        mtMeeting.setCreateRealname(imRoom.getCreateRealname());
        mtMeeting.setRoomPassword(imRoom.getRoomPassword());
        mtMeeting.setRoomType(imRoom.getRoomType());
        mtMeeting.setUsernames(imRoom.getUsernames());
    }

    /**
     * 查询会议列表
     *
     * @param mtMeeting 会议
     * @return 会议
     */
    @Override
    public List<MtMeeting> selectMtMeetingList(MtMeeting mtMeeting)
    {
        List<MtMeeting> list = mtMeetingMapper.selectMtMeetingList(mtMeeting);
        //去即讯会议中查询
        List<String> ids = list.stream().map(MtMeeting::getImMeetingId).collect(Collectors.toList());
        ImResultDto<List<ImRoom>> listImResultDto;
        try {
            listImResultDto = imMeetingSdk.listRoomByIds(ids);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询即讯会议失败：" + e.getMessage());
        }
        if (!listImResultDto.getCode().equals(200)) {
            throw new RuntimeException("查询即讯会议失败");
        }
        List<ImRoom> imRoomList = listImResultDto.getData();
        //放置到Map
        Map<String, ImRoom> map = new HashMap<>();
        for (ImRoom imRoom : imRoomList) {
            map.put(imRoom.getId(), imRoom);
        }
        //填充属性
        for (MtMeeting meeting : list) {
            ImRoom thisRoom = map.get(meeting.getImMeetingId());
            if (thisRoom == null) {
                continue;
            }
            setProperties(meeting, thisRoom);
            //根据主持人用户名查询主持人名称，用来回显
            SysUser sysUser = sysUserService.selectUserByUserName(meeting.getHostUsername());
            if (sysUser == null) {
                continue;
            }
            meeting.setHostRealname(sysUser.getNickName());
        }
        return list;
    }

    /**
     * 新增会议
     *
     * @param mtMeeting 会议
     * @return 结果
     */
    @Override
    public int insertMtMeeting(MtMeeting mtMeeting)
    {
        //先向即讯会议系统添加数据
        ImRoom imRoom = new ImRoom();
        //复制字段
        BeanUtils.copyProperties(mtMeeting, imRoom);
        ImResultDto<ImRoom> imResultDto;
        try {
            imResultDto = imMeetingSdk.saveRoom(imRoom);
        } catch (IOException e) {
            throw new RuntimeException("向即讯会议系统添加会议失败" + e.getMessage());
        }
        Integer code = imResultDto.getCode();
        if (!code.equals(200)) {
            throw new RuntimeException("向即讯会议系统添加会议失败。code：" + code);
        }
        ImRoom room = imResultDto.getData();
        //为自己系统的bean赋值
        mtMeeting.setImMeetingId(room.getId());
        return mtMeetingMapper.insertMtMeeting(mtMeeting);
    }

    /**
     * 修改会议
     *
     * @param mtMeeting 会议
     * @return 结果
     */
    @Override
    public int updateMtMeeting(MtMeeting mtMeeting)
    {
        //远程修改即讯会议的数据
        ImRoom imRoom = new ImRoom();
        //复制字段
        BeanUtils.copyProperties(mtMeeting, imRoom);
        imRoom.setId(mtMeeting.getImMeetingId());
        ImResultDto<ImRoom> imResultDto;
        try {
            imResultDto = imMeetingSdk.updateRoom(imRoom);
        } catch (IOException e) {
            throw new RuntimeException("向即讯会议系统修改会议失败" + e.getMessage());
        }
        Integer code = imResultDto.getCode();
        if (!code.equals(200)) {
            throw new RuntimeException("向即讯会议系统修改会议失败。code：" + code);
        }
        ImRoom room = imResultDto.getData();
        //为自己系统的bean赋值
        mtMeeting.setImMeetingId(room.getId());
        return mtMeetingMapper.updateMtMeeting(mtMeeting);
    }

    /**
     * 批量删除会议
     *
     * @param ids 需要删除的会议主键
     * @return 结果
     */
    @Override
    public int deleteMtMeetingByIds(Long[] ids)
    {
        return mtMeetingMapper.deleteMtMeetingByIds(ids);
    }

    /**
     * 删除会议信息
     *
     * @param id 会议主键
     * @return 结果
     */
    @Override
    public int deleteMtMeetingById(Long id)
    {
        MtMeeting mtMeeting = selectMtMeetingById(id);
        ImResultDto imResultDto;
        try {
            imResultDto = imMeetingSdk.deleteRoomById(mtMeeting.getImMeetingId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        Integer code = imResultDto.getCode();
        if (!code.equals(200)) {
            throw new RuntimeException("向即讯会议系统删除失败。code：" + code);
        }
        return mtMeetingMapper.deleteMtMeetingById(id);
    }
}
