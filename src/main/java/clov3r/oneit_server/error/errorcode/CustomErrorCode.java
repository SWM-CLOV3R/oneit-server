package clov3r.oneit_server.error.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {
  /**
   * 요청 성공
   */
  SUCCESS(OK, "요청에 성공하였습니다."),

  // 400번대
  /**
   * Request 오류
   */
  REQUEST_GENDER_ERROR(BAD_REQUEST, "성별 입력값이 잘못되었습니다."),
  REQUEST_PRICE_ERROR(BAD_REQUEST, "가격 입력값이 잘못되었습니다."),
  REQUEST_KEYWORD_ERROR(BAD_REQUEST, "키워드 입력값이 잘못되었습니다."),
  INVALID_ACCESS_STATUS(BAD_REQUEST, "접근 권한 요청값은 PRIVATE 또는 PUBLIC이어야 합니다."),
  DATE_BEFORE_NOW(BAD_REQUEST, "현재 시간 이후의 날짜를 입력해주세요."),
  S3_REQUEST_ERROR(BAD_REQUEST,"S3 이미지 요청값이 잘못되었습니다."),
  S3_FILE_EXTENSION_ERROR(BAD_REQUEST, "S3 이미지 확장자가 유효하지 않습니다."),
  NOT_EXIST_EMOJI(BAD_REQUEST, "존재하지 않는 이모지입니다."),
  NOT_EXIST_INQUIRY(BAD_REQUEST, "존재하지 않는 물어보기입니다."),
  NOT_EXIST_INQUIRY_PRODUCT(BAD_REQUEST, "존재하지 않는 물어보기 상품입니다."),
  INVALID_SELF_REQUEST(BAD_REQUEST, "자신에게 친구 요청을 보낼 수 없습니다."),
  INVALID_FRIEND_REQUEST(BAD_REQUEST, "이미 친구 요청을 보낸 상태입니다."),
  DUPLICATE_FRIEND(BAD_REQUEST, "이미 친구 관계 상태이기 때문에 요청을 보낼 수 없습니다."),
  DUPLICATE_NICKNAME(BAD_REQUEST, "중복된 닉네임입니다."),
  SEARCH_KEYWORD_ERROR(BAD_REQUEST, "검색 키워드 입력값은 2글자 이상이어야 합니다."),
  CANNOT_DELETE_OTHERS_COMMENT(BAD_REQUEST, "다른 사용자의 댓글은 삭제할 수 없습니다."),

  /**
   * token 오류
   */
  TOKEN_ERROR(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
  TOKEN_EMPTY(UNAUTHORIZED, "토큰이 비어있습니다."),
  TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
  NO_AUTHORIZATION_HEADER(UNAUTHORIZED, "Authorization Header가 없습니다."),
  INVALID_TOKEN(UNAUTHORIZED,"유효하지 않은 토큰입니다."),

  /**
   * NOT_FOUND
   */
  DATABASE_ERROR_NOT_FOUND(NOT_FOUND, "데이터베이스에서 찾을 수 없습니다."),
  PRODUCT_DTO_ERROR(NOT_FOUND, "상품 정보를 불러오는데 실패하였습니다."),
  GIFTBOX_NOT_FOUND(NOT_FOUND,  "선물바구니를 찾을 수 없습니다."),
  USER_NOT_FOUND(NOT_FOUND, "유저를 찾을 수 없습니다."),
  PRODUCT_NOT_FOUND(NOT_FOUND, "상품을 찾을 수 없습니다."),
  ALREADY_USED_INVITATION(NOT_FOUND,"이미 초대가 완료되었습니다."),
  ALREADY_PARTICIPANT_OF_GIFTBOX(NOT_FOUND, "이미 선물바구니의 참여자입니다."),
  INVITATION_NOT_FOUND(NOT_FOUND, "초대를 찾을 수 없습니다."),
  INQUIRY_NOT_FOUND(NOT_FOUND, "물어보기를 찾을 수 없습니다."),
  ALREADY_USED_INQUIRY(NOT_FOUND, "이미 응답한 물어보기입니다."),
  COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다."),

  /**
   * FORBIDDEN
   */
  NOT_MANAGER_OF_GIFTBOX(FORBIDDEN, "선물바구니의 관리자가 아닙니다."),
  NOT_PARTICIPANT_OF_GIFTBOX(FORBIDDEN,"선물바구니의 참여자가 아닙니다."),

  // 500번대
  /**
   * Service 오류
   *
   */
  S3_ERROR(INTERNAL_SERVER_ERROR, "S3에러입니다."),
  S3_UPLOAD_ERROR(INTERNAL_SERVER_ERROR,"S3 업로드가 실패하였습니다."),
  S3_DELETE_OBJECT_ERROR(INTERNAL_SERVER_ERROR, "S3 이미지 삭제가 실패하였습니다."),
  S3_GET_URL_ERROR(INTERNAL_SERVER_ERROR,"S3 이미지 URL을 가져오는데 실패하였습니다."),
  S3_URL_DECODING_ERROR(INTERNAL_SERVER_ERROR, "S3 이미지 URL 디코딩이 실패하였습니다."),
  S3_PUT_OBJECT_ERROR(INTERNAL_SERVER_ERROR, "S3 이미지 객체 생성이 실패하였습니다."),
  FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL(INTERNAL_SERVER_ERROR, "선물바구니 이미지 URL 업데이트에 실패하였습니다."),
  FAIL_TO_UPDATE_GIFTBOX(INTERNAL_SERVER_ERROR, "선물바구니 업데이트에 실패하였습니다."),


  SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버와의 연결에 실패하였습니다."),
  DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
  DATABASE_ERROR_QUERY(INTERNAL_SERVER_ERROR, "데이터베이스 쿼리 실행이 실패하였습니다."),;

  private final HttpStatus httpStatus;
  private final String message;


}
