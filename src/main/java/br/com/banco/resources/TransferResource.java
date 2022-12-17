package br.com.banco.resources;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.banco.dtos.responses.TransferResponseDto;
import br.com.banco.exceptions.BadRequestException;
import br.com.banco.services.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/transfers")
@RequiredArgsConstructor
@Api(value = "Transferência", tags = {"Serviço para o fornecimento de dados de transferência"})
public class TransferResource {
	
	private final TransferService transferService;
	
	@GetMapping("/{accountNumber}")
	@ApiOperation(value= "Retorna os dados de transferência de acordo com o número da conta bacária e filtros opcionais", httpMethod = "GET")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<TransferResponseDto> findByFilters(
			@PathVariable("accountNumber") Long accountNumber,
			@RequestParam(name = "initialDate", required = false) 
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime initialDate,
			@RequestParam(name = "finalDate", required = false) 
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finalDate,
			@RequestParam(name = "transactionOperatorName", required = false) String transactionOperatorName,
			Pageable pageable) throws BadRequestException {
		return ResponseEntity.ok(transferService.findByFilters(accountNumber, initialDate,
				finalDate, transactionOperatorName, pageable));
	}
}