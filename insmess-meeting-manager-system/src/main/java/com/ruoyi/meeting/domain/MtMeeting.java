package com.ruoyi.meeting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 会议对象 mt_meeting
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
public class MtMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 即讯会议系统的会议id */
    @Excel(name = "即讯会议系统的会议id")
    private String imMeetingId;

    /**========其他字段，来源于即讯会议系统中的字段========*/
    /** 会议名称 */
    private String name;
    /** 会议描述 */
    private String description;
    /** 主持人用户名 */
    private String hostUsername;
    /** 主持人真实姓名 */
    private String hostRealname;
    /** 创建事件 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 开始事件 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /** 会议时长（分钟） */
    private Integer duration;
    /** 创建人用户名 */
    private String createUsername;
    /** 创建人真实姓名 */
    private String createRealname;
    /** 会议密码 */
    private String roomPassword;
    /** 会议类型 0公开会议 1私密会议 */
    private Integer roomType;
    /** 会议成员（用户名） */
    private List<String> usernames;
    /**========其他字段，来源于即讯会议系统中的字段========*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImMeetingId() {
        return imMeetingId;
    }

    public void setImMeetingId(String imMeetingId) {
        this.imMeetingId = imMeetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getHostRealname() {
        return hostRealname;
    }

    public void setHostRealname(String hostRealname) {
        this.hostRealname = hostRealname;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getCreateRealname() {
        return createRealname;
    }

    public void setCreateRealname(String createRealname) {
        this.createRealname = createRealname;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
