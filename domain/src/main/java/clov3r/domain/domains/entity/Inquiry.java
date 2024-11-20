package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.InquiryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "giftbox_idx")
  private Giftbox giftbox;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_idx")
  private User user;

  @Column(name = "inquiry_status")
  @Enumerated(value = EnumType.STRING)
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
