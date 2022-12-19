package br.com.banco.resources.transfersIntegration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import br.com.banco.resources.integrations.IntegrationTests;

public class TransferResourceIntegrationTest extends IntegrationTests {

	@Test
	@Order(1)
	public void whenTryFindByFiltersWithDateGreaterThenReturnBadRequestException() throws Exception {
		mvc.perform(get("/transfers/{id}", 1)
				.param("initialDate", LocalDateTime.of(2022, Month.DECEMBER, 02, 00, 00, 00).toString())
				.param("finalDate", LocalDateTime.of(2022, Month.DECEMBER, 01, 00, 00, 00).toString())
				.headers(mockHttpHeaders())).andExpect(status().isBadRequest())
				.andExpect(jsonPath("message", is("Bad Request error")))
				.andExpect(jsonPath("errors.[0]", is("A data final tem que ser superior a data inicial")));
	}

	@Test
	@Order(2)
	public void whenTryFindByFiltersReturnBadRequestException() throws Exception {
		mvc.perform(get("/transfers/{id}", 1)
				.param("finalDate", LocalDateTime.of(2022, Month.DECEMBER, 01, 00, 00, 00).toString())
				.headers(mockHttpHeaders())).andExpect(status().isBadRequest())
				.andExpect(jsonPath("message", is("Bad Request error")))
				.andExpect(jsonPath("errors.[0]", is(
						"Para realizar a busca por data, é obrigatório o preenchimento da data inicial e data final")));
	}
	
	@Test
	@Order(3)
	public void whenFindByTransactionOperatorNameReturnOk() throws Exception {
		mvc.perform(get("/transfers/{id}", 1)
				.param("transactionOperatorName", "Beltrano")
				.param("size", "1")
				.headers(mockHttpHeaders())).andExpect(status().isOk())
				.andExpect(jsonPath("totalBalance").exists())
				.andExpect(jsonPath("balanceInPeriod").exists())
				.andExpect(jsonPath("transferResponseDataDto.totalElements").value(1))
				.andExpect(jsonPath("transferResponseDataDto.content").isArray()) 
				.andExpect(jsonPath("transferResponseDataDto.content").isNotEmpty())
				.andExpect(jsonPath("transferResponseDataDto.content[0].transferDate").exists()) 
				.andExpect(jsonPath("transferResponseDataDto.content[0].value").exists()) 
				.andExpect(jsonPath("transferResponseDataDto.content[0].type").exists()) 
				.andExpect(jsonPath("transferResponseDataDto.content[0].account").exists()) 
				.andExpect(jsonPath("transferResponseDataDto.content[0].transactionOperatorName").value("Beltrano"));
	}
	
	@Test
	@Order(4)
	public void whenFindWithoutFiltersReturnOk() throws Exception {
		mvc.perform(get("/transfers/{id}", 1)
				.headers(mockHttpHeaders())).andExpect(status().isOk())
				.andExpect(jsonPath("totalBalance").exists())
				.andExpect(jsonPath("balanceInPeriod").exists())
				.andExpect(jsonPath("transferResponseDataDto.content").isArray()) 
				.andExpect(jsonPath("transferResponseDataDto.content").isNotEmpty());
	}
}
