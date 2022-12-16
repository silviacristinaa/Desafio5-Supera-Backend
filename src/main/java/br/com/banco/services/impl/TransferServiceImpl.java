package br.com.banco.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.repositories.TransferRepository;
import br.com.banco.services.TransferService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

	private final TransferRepository transferRepository;
	private final ModelMapper modelMapper;

	@Override
	public Page<TransferResponseDto> findByFilters(Long accountNumber, LocalDateTime initialDate,
			LocalDateTime finalDate, String transactionOperatorName, Pageable pageable) {
		
		List<TransferResponseDto> response = transferRepository
				.findByAccountIdAndTransferDateBetweenAndTransactionOperatorName(accountNumber, initialDate, finalDate,
						transactionOperatorName, pageable)
				.stream().map(transfer -> modelMapper.map(transfer, TransferResponseDto.class))
				.collect(Collectors.toList());

		return new PageImpl<TransferResponseDto>(response);
	}
}
