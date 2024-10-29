package clov3r.api.common.service.common;

import clov3r.api.common.domain.DTO.FcmMessageDTO;
import clov3r.api.common.domain.DTO.PushNotificationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

  ObjectMapper objectMapper = new ObjectMapper();
  @Value("${fcm.base-url}")
  String API_URL;
  @Value("${fcm.auth-url}")
  String AUTH_URL;
  @Value("${fcm.config.path}")
  String firebaseConfigPath;

  @PostConstruct
  private String getAccessToken() throws IOException {

    GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource("firebase/"+firebaseConfigPath).getInputStream())
        .createScoped(List.of(AUTH_URL));

    googleCredentials.refreshIfExpired();
    return googleCredentials.getAccessToken().getTokenValue();
  }

  private String makeMessage(String token, String title, String body) throws JsonProcessingException {
    FcmMessageDTO fcmMessage = FcmMessageDTO.builder()
        .message(FcmMessageDTO.Message.builder()
            .token(token)
            .notification(FcmMessageDTO.Notification.builder()
                .title(title)
                .body(body)
                .build()
            ).build()).validateOnly(false).build();

    return objectMapper.writeValueAsString(fcmMessage);
  }

  public void sendMessageTo(String targetToken, String title, String body) throws IOException {
    String message = makeMessage(targetToken, title, body);
    System.out.println("message = " + message);

    OkHttpClient client = new OkHttpClient();

    RequestBody requestBody = RequestBody.create(message,
        MediaType.get("application/json; charset=utf-8"));
    Request request = new Request.Builder()
        .url(API_URL)
        .post(requestBody)
        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
    }
  }

  public void sendMessageTo(PushNotificationRequest pushNotificationRequest) throws IOException {
    String message = makeMessage(pushNotificationRequest.getToken(),
        pushNotificationRequest.getTitle(), pushNotificationRequest.getBody());

    OkHttpClient client = new OkHttpClient();

    RequestBody requestBody = RequestBody.create(message,
        MediaType.get("application/json; charset=utf-8"));
    Request request = new Request.Builder()
        .url(API_URL)
        .post(requestBody)
        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
    }
  }
}
