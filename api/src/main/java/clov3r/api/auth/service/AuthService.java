package clov3r.api.auth.service;

import clov3r.api.auth.domain.dto.KakaoFriendDTO;
import clov3r.api.auth.domain.dto.KakaoProfileDTO;
import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.auth.domain.entity.User;
import clov3r.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    /**
     * 카카오 API 서버로부터 가져온 사용자 정보를 저장하는 메소드
     * @param kakaoProfileDTO
     * @return
     */
    @Transactional
    public User createUserByKakao(KakaoProfileDTO kakaoProfileDTO) {
        // 사용자 정보 저장
        User user = new User(kakaoProfileDTO);
        userRepository.save(user);
        return user;
    }

    /**
     * 카카오 API 서버로부터 사용자 정보를 가져오는 메소드
     * @param kakaoAccessToken
     * @return
     */
    public KakaoProfileDTO getKaKaoUserInfo(String kakaoAccessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

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

    /**
     * 카카오 API 서버로부터 사용자의 친구 목록을 가져오는 메소드
     * @param kakaoAccessToken
     * @return
     */
    public KakaoFriendDTO getKakaoFriends(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

        //kakao api server end point URI
        String uri = "https://kapi.kakao.com/v1/api/talk/friends";
        KakaoFriendDTO kakaoFriendDTO = restTemplate.exchange(
                                            uri,
                                            HttpMethod.GET,
                                            new HttpEntity<>(null, headers),
                                            KakaoFriendDTO.class)
                                            .getBody();

        return kakaoFriendDTO;
    }

}
