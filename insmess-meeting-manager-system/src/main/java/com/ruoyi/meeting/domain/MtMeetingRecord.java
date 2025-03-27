package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会议录屏对象 mt_meeting_record
 * 
 * @author ruoyi
 * @date 2025-03-25
 */
public class MtMeetingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 录屏名称 */
    @Excel(name = "录屏名称")
    private String recordName;

    /** 录屏地址 */
    @Excel(name = "录屏地址")
    private String recordUrl;

    /** 会议室的id */
    @Excel(name = "会议室的id")
    private String roomId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setRecordName(String recordName) 
    {
        this.recordName = recordName;
    }

    public String getRecordName() 
    {
        return recordName;
    }

    public void setRecordUrl(String recordUrl) 
    {
        this.recordUrl = recordUrl;
    }

    public String getRecordUrl() 
    {
        return recordUrl;
    }

    public void setRoomId(String roomId) 
    {
        this.roomId = roomId;
    }

    public String getRoomId() 
    {
        return roomId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordName", getRecordName())
            .append("createTime", getCreateTime())
            .append("recordUrl", getRecordUrl())
            .append("roomId", getRoomId())
            .toString();
    }
}
