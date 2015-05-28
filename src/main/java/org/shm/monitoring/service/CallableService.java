package org.shm.monitoring.service;

import org.shm.monitoring.domain.ProjectConfiguration;

import java.util.concurrent.Callable;


public abstract class CallableService<V> implements Callable<V> {

    private ProjectConfiguration projectConfiguration;

    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    public void setProjectConfiguration(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    public CallableService(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

}
