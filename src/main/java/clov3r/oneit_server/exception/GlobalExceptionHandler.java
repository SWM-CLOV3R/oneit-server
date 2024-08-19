package clov3r.oneit_server.exception;

import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<BaseResponse<?>> handleAuthException(AuthException exception) {
    BaseResponse<?> response = new BaseResponse<>(exception.getBaseResponseStatus());
    return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
  }
}
