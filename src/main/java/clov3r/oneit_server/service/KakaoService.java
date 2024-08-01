package clov3r.oneit_server.service;

import clov3r.oneit_server.config.security.TokenProvider;
import clov3r.oneit_server.domain.DTO.AuthToken;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.DTO.KakaoProfile;
import clov3r.oneit_server.repository.KakaoRepository;
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
public class KakaoService {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    /**
     * 카카오 API 서버로부터 거져온 사용자 정보를 저장하는 메소드
     * @param kakaoProfile
     * @return
     */
    @Transactional
    public User createUser(KakaoProfile kakaoProfile) {
        // 사용자 정보 저장
        User user = new User();
        user.setEmail(kakaoProfile.getKakao_account().getEmail());
        user.setNickname(kakaoProfile.getProperties().getNickname());
        user.setProfileImgFromKakao(kakaoProfile.getProperties().getProfile_image());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    public AuthToken createToken(Long userId) {
        return tokenProvider.createToken(userId);
    }

    /**
     * 카카오 API 서버로부터 사용자 정보를 가져오는 메소드
     * @param accessToken
     * @return
     */
    public KakaoProfile getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //kakao api server end point URI
        String uri = "https://kapi.kakao.com/v2/user/me";
        KakaoProfile kakaoProfile = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        KakaoProfile.class)
                        .getBody();

        return kakaoProfile;

    }

}
