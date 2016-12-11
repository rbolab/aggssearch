package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FundShare;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FundShare entity.
 */
@SuppressWarnings("unused")
public interface FundShareRepository extends JpaRepository<FundShare,Long> {

}
