package org.shm.monitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.repository.ProjectConfigurationRepository;
import org.shm.monitoring.repository.ResponseRepository;
import org.shm.monitoring.web.rest.dto.DashboardDTO;
import org.shm.monitoring.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Response.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private ResponseRepository responseRepository;

    @Inject
    private ProjectConfigurationRepository projectConfigurationRepository;


    /**
     * GET  /dashboard -> get 10 last the responses and projectConfiguration.
     */
    @RequestMapping(value = "/dashboard",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DashboardDTO> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        DashboardDTO dashboardDTO = new DashboardDTO();

        Page<Response> page = responseRepository.findAll(PaginationUtil.generatePageRequest(0, 10));
        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();

        dashboardDTO.setProjectConfigurations(projectConfigurations);
        dashboardDTO.setResponses(page.getContent());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/responses", 0, 10);
        return new ResponseEntity<>(dashboardDTO, headers, HttpStatus.OK);

    }

}