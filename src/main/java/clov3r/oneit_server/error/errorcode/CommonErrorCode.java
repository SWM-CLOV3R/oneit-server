package clov3r.oneit_server.error.errorcode;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  REQUEST_ERROR(BAD_REQUEST, "입력값이 잘못되었습니다."),
  DATABASE_ERROR_NOT_FOUND(NOT_FOUND, "데이터베이스에서 찾을 수 없습니다."),
  SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버와의 연결에 실패하였습니다."),
  DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

}
