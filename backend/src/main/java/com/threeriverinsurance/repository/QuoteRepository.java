package com.threeriverinsurance.repository;

import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Quote entity persistence.
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    /**
     * Finds all quotes by customer email.
     *
     * @param email the customer email
     * @return list of quotes
     */
    List<Quote> findByCustomerEmail(String email);

    /**
     * Finds all quotes by status.
     *
     * @param status the quote status
     * @return list of quotes
     */
    List<Quote> findByStatus(QuoteStatus status);

    /**
     * Finds all quotes by insurance type.
     *
     * @param insuranceType the insurance type
     * @return list of quotes
     */
    List<Quote> findByInsuranceType(InsuranceType insuranceType);
}
