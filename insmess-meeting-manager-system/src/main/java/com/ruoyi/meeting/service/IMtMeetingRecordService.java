package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MtMeetingRecord;

/**
 * 会议录屏Service接口
 *
 * @author ruoyi
 * @date 2025-03-25
 */
public interface IMtMeetingRecordService
{
    /**
     * 查询会议录屏
     *
     * @param imMeetingId 会议录id
     * @return 会议录屏
     */
    public List<MtMeetingRecord> selectMtMeetingRecordByMeetingId(String imMeetingId);

}
