package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FundShare;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FundShare entity.
 */
public interface FundShareSearchRepository extends ElasticsearchRepository<FundShare, Long> {


}
