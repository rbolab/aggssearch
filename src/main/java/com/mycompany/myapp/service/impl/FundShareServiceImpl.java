package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FundShare;
import com.mycompany.myapp.repository.FundShareRepository;
import com.mycompany.myapp.repository.search.FundShareSearchRepository;
import com.mycompany.myapp.service.FundShareService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing FundShare.
 */
@Service
@Transactional
public class FundShareServiceImpl implements FundShareService{

    private final Logger log = LoggerFactory.getLogger(FundShareServiceImpl.class);

    @Inject
    private FundShareRepository fundShareRepository;

    @Inject
    private FundShareSearchRepository fundShareSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Save a fundShare.
     *
     * @param fundShare the entity to save
     * @return the persisted entity
     */
    public FundShare save(FundShare fundShare) {
        log.debug("Request to save FundShare : {}", fundShare);
        FundShare result = fundShareRepository.save(fundShare);
        fundShareSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the fundShares.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public String findAll() throws IOException {
        log.debug("Request to get all FundShares");
        //List<FundShare> result = fundShareRepository.findAll();
        SearchRequestBuilder srb = elasticsearchTemplate.getClient().prepareSearch("search_amethyst").setTypes("mutualfund")
            .setSize(215).setQuery(QueryBuilders.matchAllQuery()).setFetchSource(new String[]{"baseName", "shares.name", "shares.isin", "shares.investorType", "shares.performanceData"}, null);

        TermsBuilder aggsCurrency = AggregationBuilders.terms("aggs_currency").field("currency");
        NestedBuilder aggsInvetorType = AggregationBuilders.nested("nested_aggs").path("shares").subAggregation(AggregationBuilders.terms("investorType").field("shares.investorType"));

        srb.addAggregation(aggsCurrency);
        srb.addAggregation(aggsInvetorType);
        SearchResponse response = srb.execute().actionGet();

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        response.toXContent(builder, ToXContent.EMPTY_PARAMS);
        builder.endObject();

        return builder.string();
    }

    /**
     *  Get one fundShare by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FundShare findOne(Long id) {
        log.debug("Request to get FundShare : {}", id);
        FundShare fundShare = fundShareRepository.findOne(id);
        return fundShare;
    }

    /**
     *  Delete the  fundShare by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FundShare : {}", id);
        fundShareRepository.delete(id);
        fundShareSearchRepository.delete(id);
    }

    /**
     * Search for the fundShare corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FundShare> search(String query) {
        log.debug("Request to search FundShares for query {}", query);
        return StreamSupport
            .stream(fundShareSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
