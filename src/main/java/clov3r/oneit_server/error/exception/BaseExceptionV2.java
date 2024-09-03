package clov3r.oneit_server.error.exception;

import clov3r.oneit_server.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseExceptionV2 extends RuntimeException {

    private final ErrorCode errorCode;


}
