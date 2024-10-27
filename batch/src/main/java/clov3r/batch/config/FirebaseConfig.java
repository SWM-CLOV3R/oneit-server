package clov3r.batch.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

  @Value("${fcm.config.path}")
  private String firebaseConfigFile;
  @PostConstruct
  public void init(){
    try{
      InputStream serviceAccount = new ClassPathResource("firebase/"+firebaseConfigFile).getInputStream();
      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp이 이미 초기화되어 있지 않은 경우에만 초기화 실행
        FirebaseApp.initializeApp(options);
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}