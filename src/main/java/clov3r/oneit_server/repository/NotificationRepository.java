package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification n WHERE n.receiver.idx = :userIdx and n.notiStatus != 'READ'")
  List<Notification> findAllByUserId(Long userIdx);

}
