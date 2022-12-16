package br.com.banco.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.banco.dtos.responses.TransferResponseDto;

public interface TransferService {

	Page<TransferResponseDto> findByFilters(Long accountNumber, LocalDateTime initialDate, LocalDateTime finalDate,
			String transactionOperatorName, Pageable pageable);
}
