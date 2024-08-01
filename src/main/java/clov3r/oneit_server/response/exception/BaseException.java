package clov3r.oneit_server.response.exception;

import clov3r.oneit_server.response.BaseResponseStatus;

public class BaseException extends RuntimeException {

    public BaseException(BaseResponseStatus baseResponseStatus) {
        super(baseResponseStatus.getMessage());
    }

}
