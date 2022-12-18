package br.com.banco.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.banco.dtos.responses.TransferResponseDataDto;
import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.dtos.responses.TransferResponseDto.TransferResponseDtoBuilder;
import br.com.banco.exceptions.BadRequestException;
import br.com.banco.repositories.TransferRepository;
import br.com.banco.services.TransferService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

	private static final String MSG_ERROR_DATE_GRATER_THAN = "A data final tem que ser superior a data inicial";
	private static final String MSG_ERROR_FILLING_DATES = "Para realizar a busca por data, é obrigatório o preenchimento da "
			+ "data inicial e data final";
	
	private final TransferRepository transferRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public TransferResponseDto findByFilters(Long accountNumber, LocalDateTime initialDate,
			LocalDateTime finalDate, String transactionOperatorName, Pageable pageable) throws BadRequestException {
		
		transactionOperatorName = validateFilters(initialDate, finalDate, transactionOperatorName);
		
		List<TransferResponseDataDto> response = transferRepository
				.findByAccountIdAndTransferDateBetweenAndTransactionOperatorName(accountNumber, initialDate, finalDate,
						transactionOperatorName)
				.stream().map(transfer -> modelMapper.map(transfer, TransferResponseDataDto.class))
				.collect(Collectors.toList());
		
		TransferResponseDtoBuilder builder = TransferResponseDto.builder();
		BigDecimal balanceInPeriod = response.stream().map(TransferResponseDataDto::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		builder.balanceInPeriod(balanceInPeriod);
		
		setTotalBalance(accountNumber, initialDate, finalDate, transactionOperatorName, builder, balanceInPeriod);
		
		final int start = (int)pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), response.size());
		
		Page<TransferResponseDataDto> page = new PageImpl<>(response.subList(start, end), pageable, response.size());
		return builder.transferResponseDataDto(page).build();
	}

	private String validateFilters(LocalDateTime initialDate, LocalDateTime finalDate, String transactionOperatorName) throws BadRequestException {
		transactionOperatorName = transactionOperatorName != null && transactionOperatorName.isEmpty() ? 
				null : transactionOperatorName;
		
		if (finalDate == null && initialDate == null) {
			return transactionOperatorName;
		}
		
		if ((initialDate == null && finalDate != null) || (initialDate != null && finalDate == null)) {
			throw new BadRequestException(MSG_ERROR_FILLING_DATES);
		}
		
		if (finalDate.isBefore(initialDate)) {
			throw new BadRequestException(MSG_ERROR_DATE_GRATER_THAN);
		}
		
		return transactionOperatorName;
	}

	private void setTotalBalance(Long accountNumber, LocalDateTime initialDate, LocalDateTime finalDate,
			String transactionOperatorName, TransferResponseDtoBuilder builder, BigDecimal balanceInPeriod) {
		if (initialDate != null || finalDate != null || transactionOperatorName != null) { 
			builder.totalBalance(transferRepository.sumValueByAccountId(accountNumber)); 
		} else {
			builder.totalBalance(balanceInPeriod);
		}
	}
}
