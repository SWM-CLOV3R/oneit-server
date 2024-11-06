package clov3r.api.product.domain.status;

public enum ProductStatus {
  PENDING,  // 데이터 검수 전
  ACTIVE,  // 활성화
  INVALID,  // 비활성화
  UNSUPPORTED,  // 지원하지 않는 상태(collection에는 노출됨)
  DELETED,  // 삭제
}
