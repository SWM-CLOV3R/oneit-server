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
  INVALID_ACCESS_STATUS(false, 2005, "접근 권한 요청값은 PRIVATE 또는 PUBLIC이어야 합니다."),
  DATE_BEFORE_NOW(false, 2006, "현재 시간 이후의 날짜를 입력해주세요."),
  S3_REQUEST_ERROR(false, 2007, "S3 이미지 요청값이 잘못되었습니다."),
  S3_FILE_EXTENSION_ERROR(false, 2008, "S3 이미지 확장자가 유효하지 않습니다."),



  /**
   * 2100 : token 오류
   */
  TOKEN_ERROR(false, 2100, "토큰이 유효하지 않습니다."),
  TOKEN_EMPTY(false, 2101, "토큰이 비어있습니다."),
  TOKEN_EXPIRED(false, 2102, "토큰이 만료되었습니다."),
  NO_AUTHORIZATION_HEADER(false, 2103, "Authorization Header가 없습니다."),
  INVALID_TOKEN(false, 2104, "유효하지 않은 토큰입니다."),
  /**
   * 3000 : Service 오류
   *
   */
  S3_ERROR(false, 3000, "S3에러입니다."),
  S3_UPLOAD_ERROR(false, 3001,"S3 업로드가 실패하였습니다."),
  S3_DELETE_OBJECT_ERROR(false,3002, "S3 이미지 삭제가 실패하였습니다."),
  S3_GET_URL_ERROR(false, 3003, "S3 이미지 URL을 가져오는데 실패하였습니다."),
  S3_URL_DECODING_ERROR(false, 3004, "S3 이미지 URL 디코딩이 실패하였습니다."),
  S3_PUT_OBJECT_ERROR(false, 3005, "S3 이미지 객체 생성이 실패하였습니다."),
  FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL(false, 3006, "선물바구니 이미지 URL 업데이트에 실패하였습니다."),
  FAIL_TO_UPDATE_GIFTBOX(false, 3007, "선물바구니 업데이트에 실패하였습니다."),


  /**
   * 4000 : Database, Server오류
   */
  DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
  SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
  DATABASE_ERROR_QUERY(false, 4002, "데이터베이스 쿼리 실행이 실패하였습니다."),
  DATABASE_ERROR_NOT_FOUND(false, 4003, "데이터베이스에서 찾을 수 없습니다."),
  PRODUCT_DTO_ERROR(false, 4004, "상품 정보를 불러오는데 실패하였습니다."),
  GIFTBOX_NOT_FOUND(false, 4005, "선물바구니를 찾을 수 없습니다."),
  USER_NOT_FOUND(false, 4006, "유저를 찾을 수 없습니다."),
  PRODUCT_NOT_FOUND(false, 4007, "상품을 찾을 수 없습니다.");


  private final boolean isSuccess;
  private final int code;
  private final String message;

  private BaseResponseStatus(boolean isSuccess, int code, String message) {
    this.isSuccess = isSuccess;
    this.code = code;
    this.message = message;
  }
}
