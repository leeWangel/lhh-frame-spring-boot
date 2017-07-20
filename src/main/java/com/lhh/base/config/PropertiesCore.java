package com.lhh.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "core")
@PropertySource("classpath:/config/core.properties")
public class PropertiesCore {
    private String mode;
    private String attachmentHome;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAttachmentHome() {
        return attachmentHome;
    }

    public void setAttachmentHome(String attachmentHome) {
        this.attachmentHome = attachmentHome;
    }
}
