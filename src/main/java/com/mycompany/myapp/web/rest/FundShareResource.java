package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.FundShare;
import com.mycompany.myapp.service.FundShareService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FundShare.
 */
@RestController
@RequestMapping("/api")
public class FundShareResource {

    private final Logger log = LoggerFactory.getLogger(FundShareResource.class);

    @Inject
    private FundShareService fundShareService;

    /**
     * POST  /fund-shares : Create a new fundShare.
     *
     * @param fundShare the fundShare to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fundShare, or with status 400 (Bad Request) if the fundShare has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fund-shares")
    @Timed
    public ResponseEntity<FundShare> createFundShare(@RequestBody FundShare fundShare) throws URISyntaxException {
        log.debug("REST request to save FundShare : {}", fundShare);
        if (fundShare.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fundShare", "idexists", "A new fundShare cannot already have an ID")).body(null);
        }
        FundShare result = fundShareService.save(fundShare);
        return ResponseEntity.created(new URI("/api/fund-shares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fundShare", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fund-shares : Updates an existing fundShare.
     *
     * @param fundShare the fundShare to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fundShare,
     * or with status 400 (Bad Request) if the fundShare is not valid,
     * or with status 500 (Internal Server Error) if the fundShare couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fund-shares")
    @Timed
    public ResponseEntity<FundShare> updateFundShare(@RequestBody FundShare fundShare) throws URISyntaxException {
        log.debug("REST request to update FundShare : {}", fundShare);
        if (fundShare.getId() == null) {
            return createFundShare(fundShare);
        }
        FundShare result = fundShareService.save(fundShare);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fundShare", fundShare.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fund-shares : get all the fundShares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fundShares in body
     */
    @GetMapping("/fund-shares")
    @Timed
    public String getAllFundShares() throws IOException {
        log.debug("REST request to get all FundShares");
        return fundShareService.findAll();
    }

    /**
     * GET  /fund-shares/:id : get the "id" fundShare.
     *
     * @param id the id of the fundShare to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fundShare, or with status 404 (Not Found)
     */
    @GetMapping("/fund-shares/{id}")
    @Timed
    public ResponseEntity<FundShare> getFundShare(@PathVariable Long id) {
        log.debug("REST request to get FundShare : {}", id);
        FundShare fundShare = fundShareService.findOne(id);
        return Optional.ofNullable(fundShare)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fund-shares/:id : delete the "id" fundShare.
     *
     * @param id the id of the fundShare to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fund-shares/{id}")
    @Timed
    public ResponseEntity<Void> deleteFundShare(@PathVariable Long id) {
        log.debug("REST request to delete FundShare : {}", id);
        fundShareService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fundShare", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fund-shares?query=:query : search for the fundShare corresponding
     * to the query.
     *
     * @param query the query of the fundShare search
     * @return the result of the search
     */
    @GetMapping("/_search/fund-shares")
    @Timed
    public List<FundShare> searchFundShares(@RequestParam String query) {
        log.debug("REST request to search FundShares for query {}", query);
        return fundShareService.search(query);
    }


}
