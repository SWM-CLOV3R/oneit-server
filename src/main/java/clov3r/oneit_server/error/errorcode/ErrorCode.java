package clov3r.oneit_server.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String name();
  HttpStatus getHttpStatus();
  String getMessage();

}
