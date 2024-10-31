package clov3r.api.notification.domain.data;

public enum ActionType {
  SIGNUP_COMPLETE,      // 회원가입 완료
  FRIEND_REQUEST,       // 친구 요청
  FRIEND_ACCEPTANCE,    // 친구 수락
  GIFTBOX_INVITATION,   // 선물바구니 초대
  GIFTBOX_ACCEPTANCE,   // 선물바구니 수락
  GIFTBOX_ADD_PRODUCT,  // 선물바구니에 상품 추가
  GIFTBOX_COMMENT,      // 선물바구니에 댓글
  GIFTBOX_LIKE,         // 선물바구니에 좋아요
  GIFT_ASK_COMPLETE,    // 선물 물어보기 완료
  BIRTHDAY,             // 생일
  TIME_ATTACK_OPEN      // 타임어택 오픈
}
