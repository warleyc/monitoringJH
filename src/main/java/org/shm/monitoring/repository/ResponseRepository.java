package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.Response;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * Spring Data JPA repository for the Response entity.
 */
@Transactional
public interface ResponseRepository extends JpaRepository<Response,Long> {

    public void deleteByTypeAndDateBefore(String type,DateTime date);


    Long countByType(String type);

    Long deleteByType(String type);

    List<Response> removeByType(String type);


    @Modifying
    @Transactional
    @Query("delete from Response r where r.type = ?1")
    void deleteResponsesByType(String type);





}
