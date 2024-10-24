package clov3r.api.error.exception;

import clov3r.api.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoException extends RuntimeException{
  private final ErrorCode errorCode;
}
