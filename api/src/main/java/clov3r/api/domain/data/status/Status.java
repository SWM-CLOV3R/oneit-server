package clov3r.api.domain.data.status;

public enum Status {
    PENDING, // 데이터 검수 전
    ACTIVE, // 데이터 활성화
    INACTIVE, // 데이터 비활성화
    DELETED, // 데이터 삭제
}
