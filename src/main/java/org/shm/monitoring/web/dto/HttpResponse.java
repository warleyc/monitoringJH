package org.shm.monitoring.web.dto;

import org.shm.monitoring.domain.ProjectConfiguration;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HttpResponse")
public class HttpResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Status status;
    private int code;
    private String responseMessage;
    private String header;
    private String cookies;
    private String result;
    private long time;
    private ProjectConfiguration projectConfiguration;
    private String stackTrace;
    private int retries = 0;

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    public void setProjectConfiguration(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", responseMessage='" + responseMessage + '\'' +
                ", header='" + header + '\'' +
                ", cookies='" + cookies + '\'' +
                ", result='" + result + '\'' +
                ", time=" + time +
                ", configuration=" + projectConfiguration +
                ", stackTrace='" + stackTrace + '\'' +
                ", retries=" + retries +
                '}';
    }
}
