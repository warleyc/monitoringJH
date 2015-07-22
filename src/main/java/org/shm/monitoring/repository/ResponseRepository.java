package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.domain.enumeration.ReponseTypeEnum;
import org.shm.monitoring.web.rest.dto.SerieDTO;
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


    Long countByType(ReponseTypeEnum type);

    Long deleteByType(ReponseTypeEnum type);

    Long deleteByDateBefore(DateTime date);

    Long deleteByTypeAndDateBefore(ReponseTypeEnum type,DateTime date);

    List<Response> removeByType(ReponseTypeEnum type);

    Page<Response> findByTypeOrderByIdDesc(ReponseTypeEnum type,Pageable pageable);



    @Modifying
    @Transactional
    @Query("delete from Response r where r.type = ?1")
    void deleteResponsesByType(ReponseTypeEnum type);

}
