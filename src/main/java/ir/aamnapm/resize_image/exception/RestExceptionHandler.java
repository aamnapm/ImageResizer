package ir.aamnapm.resize_image.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
//@ControllerAdvice
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(notFoundException.getMessage());
        return buildResponseEntity(apiError);
    }

}
