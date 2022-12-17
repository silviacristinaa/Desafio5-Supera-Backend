package br.com.banco.dtos.responses;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class TransferResponseDto {
	
	private BigDecimal totalBalance;
	private BigDecimal balanceInPeriod;
	private Page<TransferResponseDataDto> transferResponseDataDto;
}