package com.liuyao.demo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config")
public class SysConfig {

    private String version;
    private String maclistfilepath;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMaclistfilepath() {
        return maclistfilepath;
    }

    public void setMaclistfilepath(String maclistfilepath) {
        this.maclistfilepath = maclistfilepath;
    }
}
