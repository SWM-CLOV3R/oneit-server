package clov3r.api.error.exception;

import clov3r.api.error.errorcode.CommonErrorCode;
import clov3r.api.error.errorcode.ErrorCode;
import clov3r.api.error.response.ErrorResponse;
import clov3r.api.service.common.SlackService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerV2 extends ResponseEntityExceptionHandler {

  private final SlackService slackService;

  @ExceptionHandler(AuthExceptionV2.class)
  public ResponseEntity<Object> handleAuthException(final AuthExceptionV2 exception) {
    final ErrorCode errorCode = exception.getErrorCode();
    log.error("AuthException : {}, errorCode : {}", exception, errorCode, exception);
    return handleExceptionInternal(errorCode);
  }

  @ExceptionHandler(BaseExceptionV2.class)
  public ResponseEntity<Object> handleBaseException(final BaseExceptionV2 exception) {
    final ErrorCode errorCode = exception.getErrorCode();
    log.error("BaseException : {}, errorCode : {}", exception, errorCode, exception);
    return handleExceptionInternal(errorCode, exception.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException exception) {
    ErrorCode errorCode = CommonErrorCode.REQUEST_ERROR;
    log.error("IllegalArgumentException : {}, errorCode : {}", exception, errorCode, exception);
    return handleExceptionInternal(errorCode, exception.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAllException(Exception exception) {
    log.warn("handleAllException", exception);

    if (!Objects.equals(MDC.get("env"), "local")) {
      HashMap<String, String> data = new HashMap<>();
      String msg = String.format("[API] ERROR\n" +
              "env : %s \n" +
              "uri : %s \n" +
              "method : %s \n" +
              "params : %s \n" +
              "auth : %s \n" +
              "exceptionMessage : %s \n" +
              "exceptionStackTrace : %s \n",
          MDC.get("env"),
          MDC.get("uri"),
          MDC.get("method"),
          MDC.get("params"),
          MDC.get("auth"),
          exception.getMessage(),
          Arrays.toString(exception.getStackTrace()).substring(0, 2000));
      data.put(exception.getClass().getName(), msg);
      slackService.sendMessage("IllegalAllException", data);
    }

    ErrorCode errorCode = CommonErrorCode.SERVER_ERROR;
    return handleExceptionInternal(errorCode);
  }


  private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(makeErrorResponse(errorCode));
  }

  private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .build();
  }

  private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {

    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(makeErrorResponse(errorCode));
  }

  private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(message)
        .build();
  }

  private ResponseEntity<Object> handleExceptionInternal(final BindException e, final ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(makeErrorResponse(e, errorCode));
  }

  private ErrorResponse makeErrorResponse(final BindException e, final ErrorCode errorCode) {
    final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(ErrorResponse.ValidationError::of)
        .collect(Collectors.toList());

    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .errors(validationErrorList)
        .build();
  }

}
