package br.com.banco.dtos.responses;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TransferResponseDto {
	
	private BigDecimal totalBalance;
	private BigDecimal balanceInPeriod;
	private Page<TransferResponseDataDto> transferResponseDataDto;
}