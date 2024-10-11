package clov3r.oneit_server.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessageDTO {
  private boolean    validateOnly;
  private Message    message;

  @Builder
  @AllArgsConstructor
  @Getter
  public static class Message {
    private Notification notification;
    private String      token;
    private Data        data;
  }

  @Builder
  @AllArgsConstructor
  @Getter
  public static class Notification {
    private String  title;
    private String  body;
  }

  @Builder
  @AllArgsConstructor
  @Getter
  public static class Data{
    private String    name;
    private String    description;
  }

}