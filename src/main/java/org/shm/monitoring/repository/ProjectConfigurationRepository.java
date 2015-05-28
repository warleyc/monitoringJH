package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;
import org.springframework.data.jpa.repository.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Spring Data JPA repository for the ProjectConfiguration entity.
 */
public interface ProjectConfigurationRepository extends JpaRepository<ProjectConfiguration,Long> {





}
