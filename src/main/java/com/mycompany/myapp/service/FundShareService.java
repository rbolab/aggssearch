package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FundShare;

import java.io.IOException;
import java.util.List;

/**
 * Service Interface for managing FundShare.
 */
public interface FundShareService {

    /**
     * Save a fundShare.
     *
     * @param fundShare the entity to save
     * @return the persisted entity
     */
    FundShare save(FundShare fundShare);

    /**
     *  Get all the fundShares.
     *
     *  @return the list of entities
     */
    String findAll() throws IOException;

    /**
     *  Get the "id" fundShare.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FundShare findOne(Long id);

    /**
     *  Delete the "id" fundShare.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the fundShare corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @return the list of entities
     */
    List<FundShare> search(String query);
}
