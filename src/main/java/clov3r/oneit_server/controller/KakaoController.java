package clov3r.oneit_server.controller;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.AuthToken;
import clov3r.oneit_server.domain.DTO.KakaoLoginDTO;
import clov3r.oneit_server.domain.DTO.KakaoProfile;
import clov3r.oneit_server.domain.collectioin.KakaoAccessToken;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.repository.KakaoRepository;
import clov3r.oneit_server.repository.UserRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.service.KakaoService;
import clov3r.oneit_server.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoRepository kakaoRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 API 목록")
    @PostMapping("/api/v1/kakao/login")
    public BaseResponse<KakaoLoginDTO> kakaoLogin(@RequestBody KakaoAccessToken kakaoAccessToken) {
        // get user info from kakao api
        KakaoProfile kakaoProfile = kakaoService.getUserInfo(kakaoAccessToken.getAccessToken());
        User user = new User();

        // check if the user exists
        if (userRepository.existsUserByEmail(kakaoProfile.getKakao_account().getEmail())) {
            // if so, return the user's access token (jwt)
            user = userRepository.findUserByEmail(kakaoProfile.getKakao_account().getEmail());
        } else {
            // if not, create a new user
            user = kakaoService.createUser(kakaoProfile);
        }
        AuthToken authToken = kakaoService.createToken(user.getIdx());
        kakaoRepository.saveRefreshToken(authToken.getRefreshToken(), user.getEmail());
        // return the user's info with access token (jwt)
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO(authToken.getAccessToken(), authToken.getRefreshToken());

        return new BaseResponse<>(kakaoLoginDTO);
    }

    // 헤더에서 토큰을 가져와 검증한 뒤에 유효하다면 user idx를 꺼내서 유저의 정보를 반환하는 API
    // @Auth 활용
    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 API 목록")
    @GetMapping("/api/v1/kakao/user")
    public BaseResponse<User> getUserInfo(
            @Parameter(description = "유저의 idx", required = false, schema = @Schema(defaultValue = "1"))
            @Auth(required = false) Long userId
    ) {
        System.out.println("kakao login userId = " + userId);
        User user = userService.getUser(userId);
        return new BaseResponse<>(user);
    }

}
