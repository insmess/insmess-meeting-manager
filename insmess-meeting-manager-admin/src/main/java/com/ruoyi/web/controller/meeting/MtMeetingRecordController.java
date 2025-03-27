package com.ruoyi.web.controller.meeting;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.meeting.domain.MtMeetingRecord;
import com.ruoyi.meeting.service.IMtMeetingRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会议录屏Controller
 *
 * @author ruoyi
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/meeting/record")
public class MtMeetingRecordController extends BaseController
{
    @Autowired
    private IMtMeetingRecordService mtMeetingRecordService;

    /**
     * 查询会议录屏列表。【imMeetingId为即讯会议系统的会议id】
     */
    @PreAuthorize("@ss.hasPermi('meeting:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(String imMeetingId)
    {
        List<MtMeetingRecord> list = mtMeetingRecordService.selectMtMeetingRecordByMeetingId(imMeetingId);
        return getDataTable(list);
    }
}
