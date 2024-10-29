package clov3r.api.common.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String name();
  HttpStatus getHttpStatus();
  String getMessage();

}
