package clov3r.api.notification.domain.entity;

import clov3r.api.auth.domain.entity.User;
import clov3r.api.common.domain.entity.BaseEntity;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "device")
public class Device {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_idx")
  private User user;

  @Column(name = "device_token")
  private String deviceToken;

  @Column(name = "device_type")
  private String deviceType;

  @Column(name = "last_logged_in_at")
  private LocalDateTime lastLoggedInAt;

  public void updateLoginAt(LocalDateTime now) {
    this.lastLoggedInAt = now;
  }
}
