package com.ruoyi.meeting.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.insmess.meeting.sdk.ImMeetingSdk;
import com.insmess.meeting.sdk.dto.ImResultDto;
import com.insmess.meeting.sdk.dto.ImRoom;
import com.insmess.meeting.sdk.dto.ImRoomRecord;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.domain.MtMeetingRecord;
import com.ruoyi.meeting.service.IMtMeetingRecordService;

/**
 * 会议录屏Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-25
 */
@Service
public class MtMeetingRecordServiceImpl implements IMtMeetingRecordService
{

    @Autowired
    private ImMeetingSdk imMeetingSdk;

    @Override
    public List<MtMeetingRecord> selectMtMeetingRecordByMeetingId(String imMeetingId) {
        //根据id查询即讯会议系统的表
        ImResultDto<List<ImRoomRecord>> imResultDto;
        try {
            imResultDto = imMeetingSdk.listRoomRecordByMeetingId(imMeetingId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询即讯会议失败：" + e.getMessage());
        }
        if (!imResultDto.getCode().equals(200)) {
            throw new RuntimeException("查询即讯会议失败");
        }
        //封装到自己的集合中
        List<MtMeetingRecord> list = new ArrayList<>();
        for (ImRoomRecord imRoomRecord : imResultDto.getData()) {
            MtMeetingRecord mtMeetingRecord = new MtMeetingRecord();
            BeanUtils.copyProperties(imRoomRecord, mtMeetingRecord);
            list.add(mtMeetingRecord);
        }
        return list;
    }
}
