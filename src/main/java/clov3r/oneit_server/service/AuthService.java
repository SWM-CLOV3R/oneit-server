package clov3r.oneit_server.service;

import static clov3r.oneit_server.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.oneit_server.domain.DTO.KakaoFriendDTO;
import clov3r.oneit_server.domain.data.status.UserStatus;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.DTO.KakaoProfileDTO;
import clov3r.oneit_server.domain.request.SignupRequest;
import clov3r.oneit_server.error.exception.BaseExceptionV2;
import clov3r.oneit_server.repository.UserRepository;
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
                kakaoProfileDTO.getProperties().getProfile_image(),
                UserStatus.ACTIVE
        );
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
