package clov3r.api.notification.repository;

import clov3r.domain.domains.entity.Notification;
import java.util.List;

public interface NotificationCustomRepository {
  List<Notification> findAllByUserId(Long userIdx);
  Notification fincByIdxAndUserIdx(Long idx, Long userIdx);
}
