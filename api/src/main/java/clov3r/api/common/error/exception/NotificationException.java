package clov3r.api.common.error.exception;

import clov3r.api.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {
  private final ErrorCode errorCode;
}