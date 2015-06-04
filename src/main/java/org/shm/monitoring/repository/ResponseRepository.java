package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Response entity.
 */
@Transactional
public interface ResponseRepository extends JpaRepository<Response,Long> {




    Long countByType(String type);

    Long deleteByType(String type);

    Long deleteByDateBefore(DateTime date);

    Long deleteByTypeAndDateBefore(String type,DateTime date);

    List<Response> removeByType(String type);

    Page<Response> findByTypeOrderByIdDesc(String type,Pageable pageable);


    @Modifying
    @Transactional
    @Query("delete from Response r where r.type = ?1")
    void deleteResponsesByType(String type);





}
