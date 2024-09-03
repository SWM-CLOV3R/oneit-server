package clov3r.oneit_server.error.exception;

import clov3r.oneit_server.error.errorcode.CommonErrorCode;
import clov3r.oneit_server.error.errorcode.ErrorCode;
import clov3r.oneit_server.error.response.ErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerV2 extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthExceptionV2.class)
  public ResponseEntity<Object> handleAuthException(final AuthExceptionV2 exception) {
    final ErrorCode errorCode = exception.getErrorCode();
    return handleExceptionInternal(errorCode);
  }

  @ExceptionHandler(BaseExceptionV2.class)
  public ResponseEntity<Object> handleBaseException(final BaseExceptionV2 exception) {
    final ErrorCode errorCode = exception.getErrorCode();
    return handleExceptionInternal(errorCode, exception.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument", e);
    ErrorCode errorCode = CommonErrorCode.REQUEST_ERROR;
    return handleExceptionInternal(errorCode, e.getMessage());
  }

//  @Override
//  public ResponseEntity<Object> handleMethodArgumentNotValid(
//      MethodArgumentNotValidException e,
//      HttpHeaders headers,
//      HttpStatus status,
//      WebRequest request) {
//    log.warn("handleIllegalArgument", e);
//    ErrorCode errorCode = CommonErrorCode.REQUEST_ERROR;
//    return handleExceptionInternal(e, errorCode);
//  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAllException(Exception ex) {
    log.warn("handleAllException", ex);
    ErrorCode errorCode = CommonErrorCode.REQUEST_ERROR;
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
