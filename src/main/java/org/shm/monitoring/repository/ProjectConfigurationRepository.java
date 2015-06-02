package org.shm.monitoring.repository;

import org.shm.monitoring.domain.ProjectConfiguration;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectConfiguration entity.
 */
public interface ProjectConfigurationRepository extends JpaRepository<ProjectConfiguration,Long> {

}
