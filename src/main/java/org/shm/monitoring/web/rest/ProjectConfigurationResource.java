package org.shm.monitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.repository.ProjectConfigurationRepository;
import org.shm.monitoring.service.ProjectConfigurationServices;
import org.shm.monitoring.web.dto.HttpResponse;
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
 * REST controller for managing ProjectConfiguration.
 */
@RestController
@RequestMapping("/api")
public class ProjectConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(ProjectConfigurationResource.class);

    @Inject
    private ProjectConfigurationRepository projectConfigurationRepository;

    @Inject
    private ProjectConfigurationServices projectConfigurationService;

    /**
     * POST  /projectConfigurations -> Create a new projectConfiguration.
     */
    @RequestMapping(value = "/projectConfigurations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectConfiguration> create(@RequestBody ProjectConfiguration projectConfiguration) throws URISyntaxException {
        log.debug("REST request to save ProjectConfiguration : {}", projectConfiguration);
        if (projectConfiguration.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new projectConfiguration cannot already have an ID").body(null);
        }
        ProjectConfiguration result = projectConfigurationRepository.save(projectConfiguration);
        return ResponseEntity.created(new URI("/api/projectConfigurations/" + projectConfiguration.getId())).body(result);
    }

    /**
     * PUT  /projectConfigurations -> Updates an existing projectConfiguration.
     */
    @RequestMapping(value = "/projectConfigurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectConfiguration> update(@RequestBody ProjectConfiguration projectConfiguration) throws URISyntaxException {
        log.debug("REST request to update ProjectConfiguration : {}", projectConfiguration);
        if (projectConfiguration.getId() == null) {
            return create(projectConfiguration);
        }
        ProjectConfiguration result = projectConfigurationRepository.save(projectConfiguration);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /projectConfigurations -> get all the projectConfigurations.
     */
    @RequestMapping(value = "/projectConfigurations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectConfiguration>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ProjectConfiguration> page = projectConfigurationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projectConfigurations", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projectConfigurations/:id -> get the "id" projectConfiguration.
     */
    @RequestMapping(value = "/projectConfigurations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectConfiguration> get(@PathVariable Long id) {
        log.debug("REST request to get ProjectConfiguration : {}", id);
        return Optional.ofNullable(projectConfigurationRepository.findOne(id))
            .map(projectConfiguration -> new ResponseEntity<>(
                projectConfiguration,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projectConfigurations/:id -> delete the "id" projectConfiguration.
     */
    @RequestMapping(value = "/projectConfigurations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ProjectConfiguration : {}", id);
        projectConfigurationRepository.delete(id);
    }

    /**
     * LAUNCH  /projectConfigurations/launch/:id -> launch the "id" projectConfiguration.
     */
    @RequestMapping(value = "/projectConfigurations/launch/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HttpResponse > launch(@PathVariable Long id) {
        log.debug("REST request to launch ProjectConfiguration : {}", id);
        ProjectConfiguration projectConfiguration=projectConfigurationRepository.findOne(id);

        return Optional.ofNullable(projectConfigurationService.testAndSaveLog(projectConfiguration))
            .map(response -> new ResponseEntity<>(
                response,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }

}
