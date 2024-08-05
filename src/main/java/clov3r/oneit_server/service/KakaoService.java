package clov3r.oneit_server.service;

import clov3r.oneit_server.config.security.TokenProvider;
import clov3r.oneit_server.domain.data.AuthToken;
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
public class KakaoService {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    /**
     * 카카오 API 서버로부터 거져온 사용자 정보를 저장하는 메소드
     * @param kakaoProfileDTO
     * @return
     */
    @Transactional
    public User createUser(KakaoProfileDTO kakaoProfileDTO) {
        // 사용자 정보 저장
        User user = new User();
        user.setEmail(kakaoProfileDTO.getKakao_account().getEmail());
        user.setNickname(kakaoProfileDTO.getProperties().getNickname());
        user.setProfileImgFromKakao(kakaoProfileDTO.getProperties().getProfile_image());
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
    public KakaoProfileDTO getUserInfo(String accessToken) {

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
