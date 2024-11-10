package clov3r.domain.error.exception;

import clov3r.domain.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {
  private final ErrorCode errorCode;
}
