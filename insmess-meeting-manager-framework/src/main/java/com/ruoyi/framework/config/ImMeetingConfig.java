package com.ruoyi.framework.config;

import com.insmess.meeting.sdk.ImMeetingSdk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImMeetingConfig {
    @Value("${im-meeting.service-url}")
    private String imMeetingUrl;

    @Bean
    public ImMeetingSdk imMeetingSdk() {
        return new ImMeetingSdk(imMeetingUrl);
    }
}
