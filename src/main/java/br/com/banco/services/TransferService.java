package br.com.banco.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.exceptions.BadRequestException;

public interface TransferService {

	TransferResponseDto findByFilters(Long accountNumber, LocalDateTime initialDate, LocalDateTime finalDate,
			String transactionOperatorName, Pageable pageable) throws BadRequestException;
}