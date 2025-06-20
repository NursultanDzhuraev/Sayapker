package pro.sayapker.exception.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(HttpStatus httpStatus, String exceptionClassName, String message) {
}
