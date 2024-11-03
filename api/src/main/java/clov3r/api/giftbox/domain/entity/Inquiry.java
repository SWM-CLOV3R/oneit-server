package clov3r.api.giftbox.domain.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.giftbox.domain.status.InquiryStatus;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.auth.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inquiry")
public class Inquiry extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "giftbox_idx")
  private Giftbox giftbox;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

  @Column(name = "inquiry_status")
  @Enumerated(value = STRING)
  private InquiryStatus inquiryStatus;

  private String target;

  public Inquiry(Giftbox giftbox, User user, InquiryStatus inquiryStatus, String target) {
    this.giftbox = giftbox;
    this.user = user;
    this.inquiryStatus = inquiryStatus;
    this.target = target;
    this.createBaseEntity();
  }
}
