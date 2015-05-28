package org.shm.monitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.repository.ResponseRepository;
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
public class ResponseResource {

    private final Logger log = LoggerFactory.getLogger(ResponseResource.class);

    @Inject
    private ResponseRepository responseRepository;

    /**
     * POST  /responses -> Create a new response.
     */
    @RequestMapping(value = "/responses",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Response response) throws URISyntaxException {
        log.debug("REST request to save Response : {}", response);
        if (response.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new response cannot already have an ID").build();
        }
        responseRepository.save(response);
        return ResponseEntity.created(new URI("/api/responses/" + response.getId())).build();
    }

    /**
     * PUT  /responses -> Updates an existing response.
     */
    @RequestMapping(value = "/responses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Response response) throws URISyntaxException {
        log.debug("REST request to update Response : {}", response);
        if (response.getId() == null) {
            return create(response);
        }
        responseRepository.save(response);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /responses -> get all the responses.
     */
    @RequestMapping(value = "/responses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Response>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Response> page = responseRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/responses", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /responses/:id -> get the "id" response.
     */
    @RequestMapping(value = "/responses/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Response> get(@PathVariable Long id) {
        log.debug("REST request to get Response : {}", id);
        return Optional.ofNullable(responseRepository.findOne(id))
            .map(response -> new ResponseEntity<>(
                response,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /responses/:id -> delete the "id" response.
     */
    @RequestMapping(value = "/responses/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Response : {}", id);
        responseRepository.delete(id);
    }
}
