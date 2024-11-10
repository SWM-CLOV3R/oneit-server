package clov3r.domain.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String name();
  HttpStatus getHttpStatus();
  String getMessage();

}
