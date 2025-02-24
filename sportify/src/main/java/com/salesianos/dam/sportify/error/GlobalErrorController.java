package com.salesianos.dam.sportify.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.extern.java.Log;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;

@Log
@RestControllerAdvice
public class GlobalErrorController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        List<ApiValidationSubError> subErrors =
                ex.getConstraintViolations().stream()
                        .map(ApiValidationSubError::from)
                        .toList();

        result.setProperty("invalid-params", subErrors);

        return result;

    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación");

        List<ApiValidationSubError> subErrors =
                ex.getAllErrors().stream()
                        .map(ApiValidationSubError::from)
                        .toList();

        result.setProperty("invalid-params", subErrors);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result);
    }

    @Builder
    record ApiValidationSubError(
            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String field,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Object rejectedValue
    ) {

        public ApiValidationSubError(String object, String message) {
            this(object, message, null, null);
        }

        public static ApiValidationSubError from(ObjectError error) {

            ApiValidationSubError result = null;

            if (error instanceof FieldError fieldError) {
                result = ApiValidationSubError.builder()
                        .object(fieldError.getObjectName())
                        .message(fieldError.getDefaultMessage())
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .build();
            } else {
                result = ApiValidationSubError.builder()
                        .object(error.getObjectName())
                        .message(error.getDefaultMessage())
                        .build();
            }

            return result;


        }

        public static ApiValidationSubError from(ConstraintViolation v) {
            return ApiValidationSubError.builder()
                    .message(v.getMessage())
                    .rejectedValue(v.getInvalidValue())
                    .object(v.getRootBean().getClass().getSimpleName())
                    .field(
                            Optional.ofNullable(v.getPropertyPath())
                                    .map(PathImpl.class::cast)
                                    .map(PathImpl::getLeafNode)
                                    .map(NodeImpl::asString)
                                    .orElse("unknown")
                    )
                    .build();
        }


    }


    @ExceptionHandler(NoticiaNotFoundException.class)
    public ProblemDetail handleNoticiaNotFoundException(EntidadNoEncontradaException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(ex.getStatus(), ex.getMessage());
        result.setTitle("Noticia no encontrada");
        result.setProperty("author", "Manuel");

        return result;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUsuarioNotFoundException(EntidadNoEncontradaException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(ex.getStatus(), ex.getMessage());
        result.setTitle("Usuario no encontrado");
        result.setProperty("author", "Manuel");

        return result;
    }

    @ExceptionHandler(UnauthorizedEditException.class)
    public ProblemDetail handleUnauthorizedEditException(UnauthorizedEditException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(ex.getStatus(), ex.getMessage());
        result.setTitle("Acceso denegado, no puedes editar esta noticia");
        return result;
    }
    @ExceptionHandler(UnauthorizedDeleteException.class)
    public ProblemDetail handleUnauthorizedDeleteException(UnauthorizedEditException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(ex.getStatus(), ex.getMessage());
        result.setTitle("Acceso denegado, no puedes borrar esta noticia");
        return result;
    }

    @ExceptionHandler(DeporteNotFoundException.class)
    public ProblemDetail handleNoticiaNotFoundException(DeporteNotFoundException ex) {
        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(ex.getStatus(), ex.getMessage());
        result.setTitle("Deporte no encontrado");
        result.setProperty("author", "Manuel");

        return result;
    }

}
