package pro.sayapker.exception.handler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.sayapker.exception.AlreadyExistsException;
import pro.sayapker.exception.BadRequestException;
import pro.sayapker.exception.ForBiddenException;
import pro.sayapker.exception.NotFoundException;
import pro.sayapker.exception.response.ExceptionResponse;


@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotFoundException notFoundException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND).
                exceptionClassName(NotFoundException.class.getSimpleName()).
                message(notFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(ForBiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse forbidden(ForBiddenException forbiddenException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN).
                exceptionClassName(ForBiddenException.class.getSimpleName()).
                message(forbiddenException.getMessage())
                .build();
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badRequest(BadRequestException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST).
                exceptionClassName(e.getClass().getSimpleName()).
                message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ExceptionResponse methodArgNotValidType(AlreadyExistsException methodArgumentNotValidTypeException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.ALREADY_REPORTED).
                exceptionClassName(methodArgumentNotValidTypeException.getClass().getSimpleName()).
                message(methodArgumentNotValidTypeException.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .build();
    }


}
