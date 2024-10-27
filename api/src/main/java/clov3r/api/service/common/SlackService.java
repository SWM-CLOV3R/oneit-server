package clov3r.api.service.common;

import static com.slack.api.webhook.WebhookPayloads.payload;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlackService {

  @Value("${webhook.slack.url}")
  private String SLACK_WEBHOOK_URL;

  private final Slack slackClient = Slack.getInstance();

  /**
   * 슬랙 메시지 전송
   **/
  public void sendMessage(String title, HashMap<String, String> data) {
    try {
      slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p
          .text(title) // 메시지 제목
          .attachments(
              List.of(
              Attachment.builder()
                  .fields( // 메시지 본문 내용
                      data.keySet().stream().map(key -> generateSlackField(key, data.get(key)))
                          .toList()
                  )
                  .build())))
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Slack Field 생성
   **/
  private Field generateSlackField(String title, String value) {
    return Field.builder()
        .title(title)
        .value(value)
        .valueShortEnough(false)
        .build();
  }
}
