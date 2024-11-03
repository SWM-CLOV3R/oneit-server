package clov3r.batch.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEventDTO {
  private Long receiverIdx;
  private String receiverName;
  private String receiverDeviceToken;
  private Long senderIdx;
  private String senderName;
  private String title;
  private String content;
}
