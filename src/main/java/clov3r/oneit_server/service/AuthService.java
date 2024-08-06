package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.DTO.KakaoProfileDTO;
import clov3r.oneit_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    /**
     * 카카오 API 서버로부터 거져온 사용자 정보를 저장하는 메소드
     * @param kakaoProfileDTO
     * @return
     */
    @Transactional
    public User createUserByKakao(KakaoProfileDTO kakaoProfileDTO) {
        // 사용자 정보 저장
        User user = new User(
                kakaoProfileDTO.getKakao_account().getEmail(),
                kakaoProfileDTO.getProperties().getNickname(),
                kakaoProfileDTO.getProperties().getProfile_image()
        );
        userRepository.save(user);
        return user;
    }

    /**
     * 카카오 API 서버로부터 사용자 정보를 가져오는 메소드
     * @param accessToken
     * @return
     */
    public KakaoProfileDTO getKaKaoUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //kakao api server end point URI
        String uri = "https://kapi.kakao.com/v2/user/me";
        KakaoProfileDTO kakaoProfileDTO = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        KakaoProfileDTO.class)
                        .getBody();

        return kakaoProfileDTO;

    }

}
