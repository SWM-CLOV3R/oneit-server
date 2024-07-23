package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.DTO.KakaoProfile;
import clov3r.oneit_server.repository.KakaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoRepository kakaoRepository;
    private final RestTemplate restTemplate;

    /**
     * 카카오 API 서버로부터 거져온 사용자 정보를 저장하는 메소드
     * @param kakaoAccessToken
     * @return
     */
    @Transactional
    public User createUser(String kakaoAccessToken) {
        KakaoProfile kakaoProfile = getUserInfo(kakaoAccessToken);
        User user = new User();
        user.setEmail(kakaoProfile.getKakao_account().getEmail());
        user.setNickname(kakaoProfile.getProperties().getNickname());
        user.setProfileImg(kakaoProfile.getProperties().getProfile_image());
        user.setKakaoAccessToken(kakaoAccessToken);

        // jwt 토큰 생성
//        user.setJwt(jwtService.createJwt(user.getEmail());
        kakaoRepository.save(user);
        return user;
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

    public User getUser(String kakaoAccessToken) {
        return kakaoRepository.findUser(kakaoAccessToken);
    }
}
