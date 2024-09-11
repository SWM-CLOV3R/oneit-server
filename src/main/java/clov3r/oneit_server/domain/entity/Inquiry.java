package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

import clov3r.oneit_server.domain.data.status.InquiryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inquiry")
public class Inquiry extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @Column(name = "giftbox_idx")
  private Long giftboxIdx;

  @Column(name = "user_idx")
  private Long userIdx;

  @Column(name = "inquiry_status")
  @Enumerated(value = STRING)
  private InquiryStatus InquiryStatus;

  private String target;

  public Inquiry(Long giftboxIdx, Long userIdx, InquiryStatus inquiryStatus, String target) {
    this.giftboxIdx = giftboxIdx;
    this.userIdx = userIdx;
    this.InquiryStatus = inquiryStatus;
    this.target = target;
    this.createBaseEntity();
  }
}
