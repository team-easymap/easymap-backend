package com.easymap.easymap.handler.exception;

import com.easymap.easymap.dto.response.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ResponseDto> handleValidationException(Exception e) {
        return ResponseDto.validationFail();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseDto.databaseError();
    }

    @ExceptionHandler({AuthenticationException.class, ExpiredJwtException.class})
    public ResponseEntity<ResponseDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseDto.authenticationFail();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDto> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseDto.notFound();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseDto.notFound();
    }

    @ExceptionHandler(NickNameDuplicatedException.class)
    public ResponseEntity<ResponseDto> handleCommonException(RuntimeException e) {
        return ResponseDto.conflict(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGenericException(Exception e) {
        e.printStackTrace();
        return ResponseDto.internalServerError();
    }
}
