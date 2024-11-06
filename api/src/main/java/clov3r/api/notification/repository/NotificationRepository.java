package clov3r.api.notification.repository;

import clov3r.domain.domains.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {

}
