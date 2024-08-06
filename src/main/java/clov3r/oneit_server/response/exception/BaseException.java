package clov3r.oneit_server.response.exception;

import clov3r.oneit_server.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    BaseResponseStatus baseResponseStatus;

    public BaseException(BaseResponseStatus baseResponseStatus) {
        super(baseResponseStatus.getMessage());
        this.baseResponseStatus = baseResponseStatus;
    }

}
