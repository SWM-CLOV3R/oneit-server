package clov3r.api.notification.repository;

import clov3r.domain.domains.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

  @Query("select d from Device d where d.user.idx = :userIdx")
  Device findByUserId(Long userIdx);

}
