package br.com.banco.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferResponseDataDto {

	private Long id;
	private LocalDateTime transferDate;
	private BigDecimal value;
	private String type;
	private String transactionOperatorName;
	private AccountResponseDto account;
}