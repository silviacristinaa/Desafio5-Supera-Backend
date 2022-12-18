package br.com.banco.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.dtos.responses.AccountResponseDto;
import br.com.banco.dtos.responses.TransferResponseDataDto;
import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.exceptions.BadRequestException;
import br.com.banco.services.TransferService;

@ExtendWith(SpringExtension.class)
public class TransferResourceTest {
	
	private static final long ID = 1L;
	private static final String NAME = "Test";
	private static final String TYPE = "SAQUE";
	private static final int INDEX = 0;
	
	private TransferResponseDataDto transferResponseDataDto; 
	private TransferResponseDto transferResponseDto;
	private AccountResponseDto accountResponseDto; 
	private LocalDateTime dateTime;
	
	@InjectMocks
	private TransferResource transferResource;

	@Mock
	private TransferService transferService;
	
	@BeforeEach
	void setUp() {
		dateTime = LocalDateTime.now();
		accountResponseDto = new AccountResponseDto(ID, NAME); 
		transferResponseDataDto = new TransferResponseDataDto(ID, dateTime, BigDecimal.TEN, TYPE, 
				NAME, accountResponseDto);	
		
		List<TransferResponseDataDto> responseDataDto = new ArrayList<>(); 
		responseDataDto.add(transferResponseDataDto);
		
		Page<TransferResponseDataDto> page = new PageImpl<>(responseDataDto);
				
		transferResponseDto = new TransferResponseDto(new BigDecimal("100.00"), BigDecimal.TEN, page);
	}
	
	
	@Test
	void whenFindByFiltersReturnOneTransferResponseDto() throws BadRequestException {
		when(transferService.findByFilters(anyLong(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
			.thenReturn(transferResponseDto); 
		
		ResponseEntity<TransferResponseDto> response = transferResource.findByFilters(ID, LocalDateTime.now(), 
				LocalDateTime.now(), NAME, Pageable.unpaged());

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(TransferResponseDto.class, response.getBody().getClass());
		
		assertEquals(new BigDecimal("100.00"), response.getBody().getTotalBalance());
		assertEquals(BigDecimal.TEN, response.getBody().getBalanceInPeriod());
		
		assertEquals(ID, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getId());
		assertEquals(dateTime, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getTransferDate());
		assertEquals(BigDecimal.TEN, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getValue());
		assertEquals(TYPE, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getType());
		assertEquals(NAME, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getTransactionOperatorName());
		
		assertEquals(ID, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getAccount().getId());
		assertEquals(NAME, response.getBody().getTransferResponseDataDto().getContent().get(INDEX).getAccount().getResponsibleName());
	}
}
