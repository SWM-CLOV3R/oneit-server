package clov3r.api.repository;

import clov3r.api.domain.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification n WHERE n.receiver.idx = :userIdx ORDER BY n.createdAt DESC")
  List<Notification> findAllByUserId(Long userIdx);

}
