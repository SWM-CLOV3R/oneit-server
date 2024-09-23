package clov3r.oneit_server.legacy.exception;

import clov3r.oneit_server.legacy.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
  BaseResponseStatus baseResponseStatus;

  public AuthException(BaseResponseStatus baseResponseStatus) {
    super(baseResponseStatus.getMessage());
    this.baseResponseStatus = baseResponseStatus;
  }

}
