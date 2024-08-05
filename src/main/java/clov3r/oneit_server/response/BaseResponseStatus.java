package clov3r.oneit_server.response;

import lombok.Getter;
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 :요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request오류
     */
    REQUEST_ERROR(false, 2000, "입력값이 잘못되었습니다."),
    REQUEST_GENDER_ERROR(false, 2001, "성별 입력값이 잘못되었습니다."),
    REQUEST_PRICE_ERROR(false, 2002, "가격 입력값이 잘못되었습니다."),
    REQUEST_KEYWORD_ERROR(false, 2003, "키워드 입력값이 잘못되었습니다."),
    S3_ERROR(false, 2004, "S3에러입니다."),

    /**
     * 2100 : token 오류
     */
    TOKEN_ERROR(false, 2100, "토큰이 유효하지 않습니다."),
    TOKEN_EMPTY(false, 2101, "토큰이 비어있습니다."),
    TOKEN_EXPIRED(false, 2102, "토큰이 만료되었습니다."),

    /**
     * 4000 : Database, Server오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    DATABASE_ERROR_QUERY(false, 4002, "데이터베이스 쿼리에 실패하였습니다."),
    DATABASE_ERROR_NOT_FOUND(false, 4003, "데이터베이스에서 찾을 수 없습니다."),
    PRODUCT_DTO_ERROR(false, 4004, "상품 정보를 불러오는데 실패하였습니다."),
    GIFTBOX_NOT_FOUND(false, 4005, "선물바구니를 찾을 수 없습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
