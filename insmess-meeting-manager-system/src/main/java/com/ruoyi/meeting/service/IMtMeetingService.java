package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MtMeeting;

/**
 * 会议Service接口
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
public interface IMtMeetingService 
{
    /**
     * 查询会议
     * 
     * @param id 会议主键
     * @return 会议
     */
    public MtMeeting selectMtMeetingById(Long id);

    /**
     * 查询会议列表
     * 
     * @param mtMeeting 会议
     * @return 会议集合
     */
    public List<MtMeeting> selectMtMeetingList(MtMeeting mtMeeting);

    /**
     * 新增会议
     * 
     * @param mtMeeting 会议
     * @return 结果
     */
    public int insertMtMeeting(MtMeeting mtMeeting);

    /**
     * 修改会议
     * 
     * @param mtMeeting 会议
     * @return 结果
     */
    public int updateMtMeeting(MtMeeting mtMeeting);

    /**
     * 批量删除会议
     * 
     * @param ids 需要删除的会议主键集合
     * @return 结果
     */
    public int deleteMtMeetingByIds(Long[] ids);

    /**
     * 删除会议信息
     * 
     * @param id 会议主键
     * @return 结果
     */
    public int deleteMtMeetingById(Long id);
}
