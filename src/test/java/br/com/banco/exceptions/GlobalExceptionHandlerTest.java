package br.com.banco.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {

	private static final String EXCEPTION_MSG_UNEXPECTED_ERROR = "Unexpected error";
	private static final String EXCEPTION_MSG_BAD_REQUEST = "Bad Request error";

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Test
	void whenExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.processException(new Exception(EXCEPTION_MSG_UNEXPECTED_ERROR));

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(EXCEPTION_MSG_UNEXPECTED_ERROR, response.getBody().getMessage());
		assertEquals(EXCEPTION_MSG_UNEXPECTED_ERROR, response.getBody().getErrors().get(0));
	}

	@Test
	void whenBadRequestExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodBadRequestException(new BadRequestException("A data final tem que ser superior a data inicial"));

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(EXCEPTION_MSG_BAD_REQUEST, response.getBody().getMessage());
		assertEquals("A data final tem que ser superior a data inicial", response.getBody().getErrors().get(0));
	}
}