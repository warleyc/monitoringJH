package org.shm.monitoring.web.rest.dto;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;

import java.util.List;

public class DashboardDTO {

    List<Response> responses;

    List<ProjectConfiguration> projectConfigurations;

    public DashboardDTO(List<Response> responses ,List<ProjectConfiguration> projectConfigurations) {
        this.responses = responses;
        this.projectConfigurations = projectConfigurations;
    }

    @JsonCreator
    public DashboardDTO() {
    }

    public List<ProjectConfiguration> getProjectConfigurations() {
        return projectConfigurations;
    }

    public void setProjectConfigurations(List<ProjectConfiguration> projectConfigurations) {
        this.projectConfigurations = projectConfigurations;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "DashboardDTO{" +
            "responses=" + responses +
            ", projectConfigurations=" + projectConfigurations +
            '}';
    }
}
