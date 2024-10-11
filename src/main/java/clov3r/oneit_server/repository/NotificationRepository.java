package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification n WHERE n.user.idx = :userIdx and n.readAt = null")
  void findAllByUserId(Long userIdx);

}
