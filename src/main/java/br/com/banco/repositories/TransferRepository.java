package br.com.banco.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.banco.entities.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>{

	@Query("SELECT t FROM Transfer t JOIN t.account a WHERE a.id = :accountId "
			+ "and ((:initialDate is null or :finalDate is null) or t.transferDate BETWEEN :initialDate AND :finalDate) "
			+ "and (:transactionOperatorName is null or t.transactionOperatorName = :transactionOperatorName)")
	List<Transfer> findByAccountIdAndTransferDateBetweenAndTransactionOperatorName(
			@Param("accountId") Long accountId,
			@Param("initialDate") LocalDateTime initialDate,
			@Param("finalDate") LocalDateTime finalDate, 
			@Param("transactionOperatorName") String transactionOperatorName);
	
	@Query("SELECT COALESCE(sum(t.value), 0) FROM Transfer t JOIN t.account a WHERE a.id = :accountId")
	BigDecimal sumValueByAccountId(@Param("accountId") Long accountId);
}