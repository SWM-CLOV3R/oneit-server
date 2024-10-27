package clov3r.api.domain.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

  public Device() {

  }
  public void updateLoginAt(LocalDateTime now) {
    this.lastLoggedInAt = now;
  }
}
