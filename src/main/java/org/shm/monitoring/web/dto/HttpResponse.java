package org.shm.monitoring.web.dto;

import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HttpResponse")
public class HttpResponse extends Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private Status status;

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

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

}
