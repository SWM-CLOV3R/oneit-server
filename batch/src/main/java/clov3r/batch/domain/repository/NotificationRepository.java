package clov3r.batch.domain.repository;

import clov3r.batch.domain.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Override
//  @Query("SELECT n FROM Notification n WHERE n.notiStatus = 'CREATED'")
  List<Notification> findAll();

}
