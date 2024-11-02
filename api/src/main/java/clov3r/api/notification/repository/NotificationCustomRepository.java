package clov3r.api.notification.repository;

import clov3r.api.notification.domain.entity.Notification;
import java.util.List;

public interface NotificationCustomRepository {
  List<Notification> findAllByUserId(Long userIdx);
}
