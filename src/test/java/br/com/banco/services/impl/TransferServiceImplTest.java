package br.com.banco.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.dtos.responses.AccountResponseDto;
import br.com.banco.dtos.responses.TransferResponseDataDto;
import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.entities.Account;
import br.com.banco.entities.Transfer;
import br.com.banco.enums.TypeEnum;
import br.com.banco.exceptions.BadRequestException;
import br.com.banco.repositories.TransferRepository;

@ExtendWith(SpringExtension.class)
public class TransferServiceImplTest {
	
	private static final long ID = 1L;
	private static final String NAME = "Test";
	private static final String TYPE = "SAQUE";
	private static final int SIZE = 1;
	private static final int INDEX = 0;
	
	private LocalDateTime dateTime; 
	private Account account;
	private Transfer transfer;
	private AccountResponseDto accountResponseDto;
	private TransferResponseDataDto transferResponseDataDto;
	
	@InjectMocks
	private TransferServiceImpl trasferServiceImpl;

	@Mock
	private TransferRepository transferRepository;

	@Mock
	private ModelMapper modelMapper;
	
	@BeforeEach
	void setUp() {
		dateTime = LocalDateTime.now(); 
		account = new Account(ID, NAME); 
		transfer = new Transfer(ID, dateTime, BigDecimal.TEN, TypeEnum.SAQUE, NAME, account);
		accountResponseDto = new AccountResponseDto(ID, NAME); 
		transferResponseDataDto = new TransferResponseDataDto(ID, dateTime, BigDecimal.TEN, TYPE, NAME, accountResponseDto);	
	}

	@Test
	void whenFindByFiltersReturnOneTransferResponseDto() throws BadRequestException {
		when(transferRepository.findByAccountIdAndTransferDateBetweenAndTransactionOperatorName(anyLong(), Mockito.any(), 
				Mockito.any(), Mockito.any())).thenReturn(List.of(transfer));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(transferResponseDataDto);
		
		TransferResponseDto response = trasferServiceImpl.findByFilters(ID, null, null, null, Pageable.ofSize(SIZE));
		
		assertNotNull(response);
		
		assertEquals(TransferResponseDto.class, response.getClass());
		assertEquals(BigDecimal.TEN, response.getTotalBalance()); 
		assertEquals(BigDecimal.TEN, response.getBalanceInPeriod()); 
		
		assertEquals(SIZE, response.getTransferResponseDataDto().getSize());
		assertEquals(ID, response.getTransferResponseDataDto().getContent().get(INDEX).getId());
		assertEquals(dateTime, response.getTransferResponseDataDto().getContent().get(INDEX).getTransferDate());
		assertEquals(BigDecimal.TEN, response.getTransferResponseDataDto().getContent().get(INDEX).getValue());
		assertEquals(TYPE, response.getTransferResponseDataDto().getContent().get(INDEX).getType());
		assertEquals(NAME, response.getTransferResponseDataDto().getContent().get(INDEX).getTransactionOperatorName());
		assertEquals(ID, response.getTransferResponseDataDto().getContent().get(INDEX).getAccount().getId());
		assertEquals(NAME, response.getTransferResponseDataDto().getContent().get(INDEX).getAccount().getResponsibleName());
	}
	
	@Test
	void whenFindByFiltersReturnDifferentTotalBalance() throws BadRequestException {
		when(transferRepository.findByAccountIdAndTransferDateBetweenAndTransactionOperatorName(anyLong(), Mockito.any(), 
				Mockito.any(), Mockito.any())).thenReturn(List.of(transfer));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(transferResponseDataDto);
		when(transferRepository.sumValueByAccountId(anyLong())).thenReturn(BigDecimal.ONE);
		
		TransferResponseDto response = trasferServiceImpl.findByFilters(ID, null, null, NAME, Pageable.ofSize(SIZE));
		
		assertNotNull(response);
		
		assertEquals(TransferResponseDto.class, response.getClass());
		assertEquals(BigDecimal.ONE, response.getTotalBalance()); 
		assertEquals(BigDecimal.TEN, response.getBalanceInPeriod()); 
		
		assertEquals(SIZE, response.getTransferResponseDataDto().getSize());
		assertEquals(ID, response.getTransferResponseDataDto().getContent().get(INDEX).getId());
		assertEquals(dateTime, response.getTransferResponseDataDto().getContent().get(INDEX).getTransferDate());
		assertEquals(BigDecimal.TEN, response.getTransferResponseDataDto().getContent().get(INDEX).getValue());
		assertEquals(TYPE, response.getTransferResponseDataDto().getContent().get(INDEX).getType());
		assertEquals(NAME, response.getTransferResponseDataDto().getContent().get(INDEX).getTransactionOperatorName());
		assertEquals(ID, response.getTransferResponseDataDto().getContent().get(INDEX).getAccount().getId());
		assertEquals(NAME, response.getTransferResponseDataDto().getContent().get(INDEX).getAccount().getResponsibleName());
	}
	
	@Test
	void whenTryFindByFiltersWithDateGreaterThenReturnBadRequestException() throws BadRequestException {
		
		BadRequestException exception = assertThrows(BadRequestException.class, () -> trasferServiceImpl.findByFilters(ID, 
				LocalDateTime.of(2022, Month.DECEMBER, 02, 00, 00, 00), LocalDateTime.of(2022, Month.DECEMBER, 01, 00, 00, 00), 
				null, null));

		assertEquals(String.format("A data final tem que ser superior a data inicial"), exception.getMessage());
	}
	
	@Test
	void whenTryFindByFiltersReturnBadRequestException() throws BadRequestException {
		
		BadRequestException exception = assertThrows(BadRequestException.class, () -> trasferServiceImpl.findByFilters(ID, 
				dateTime, null, null, null));

		assertEquals(String.format("Para realizar a busca por data, é obrigatório o preenchimento da data inicial"
				+ " e data final"), exception.getMessage());
	}
}
