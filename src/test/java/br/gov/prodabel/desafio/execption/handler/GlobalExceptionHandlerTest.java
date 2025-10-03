package br.gov.prodabel.desafio.execption.handler;

import static org.junit.jupiter.api.Assertions.*;

import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        String errorMsg = "Recurso não encontrado";
        ResourceNotFoundException ex = new ResourceNotFoundException(errorMsg);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(errorMsg, response.getBody().getMessage());
    }
}