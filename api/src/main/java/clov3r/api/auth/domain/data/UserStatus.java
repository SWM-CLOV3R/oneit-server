package clov3r.api.auth.domain.data;

public enum UserStatus {
  ACTIVE,  // 활성화
  INACTIVE,  // 비활성화(탈퇴 7일 이내)
  DELETED  // 삭제(탈퇴 7일 이후)
}
