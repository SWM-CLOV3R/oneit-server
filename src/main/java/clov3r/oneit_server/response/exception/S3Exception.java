package clov3r.oneit_server.response.exception;

import clov3r.oneit_server.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;

public class S3Exception extends RuntimeException {

    private final BaseResponseStatus baseResponseStatus;

    public S3Exception(BaseResponseStatus baseResponseStatus) {
        this.baseResponseStatus = baseResponseStatus;
    }

}
