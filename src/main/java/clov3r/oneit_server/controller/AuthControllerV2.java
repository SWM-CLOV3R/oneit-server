package clov3r.oneit_server.controller;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.config.security.TokenProvider;
import clov3r.oneit_server.domain.DTO.KakaoFriendDTO;
import clov3r.oneit_server.domain.DTO.KakaoLoginDTO;
import clov3r.oneit_server.domain.DTO.KakaoProfileDTO;
import clov3r.oneit_server.domain.data.AuthToken;
import clov3r.oneit_server.domain.data.status.UserStatus;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.request.KakaoAccessToken;
import clov3r.oneit_server.repository.AuthRepository;
import clov3r.oneit_server.repository.UserRepository;
import clov3r.oneit_server.service.AuthService;
import clov3r.oneit_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerV2 {

    private final AuthService authService;
    private final UserService userService;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;


    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 관련 API 목록")
    @Operation(summary = "카카오 로그인", description = "유저의 카카오 액세스 토큰을 입력받아 자체 서비스에서 카카오 로그인을 진행합니다.")
    @PostMapping("/api/v2/kakao/login")
    public ResponseEntity<KakaoLoginDTO> kakaoLogin(@RequestBody KakaoAccessToken kakaoAccessToken) {
        KakaoProfileDTO kakaoProfileDTO = authService.getKaKaoUserInfo(kakaoAccessToken.getAccessToken());
        User user;
        if (userRepository.existsUserByEmail(kakaoProfileDTO.getKakao_account().getEmail())) {
            user = userRepository.findUserByEmail(kakaoProfileDTO.getKakao_account().getEmail());
            if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                user.setStatus(UserStatus.ACTIVE); 
            }
        } else {
            user = authService.createUserByKakao(kakaoProfileDTO);
        }
        AuthToken authToken = tokenProvider.createToken(user.getIdx());
        authRepository.saveRefreshToken(authToken.getRefreshToken(), user.getEmail());

        // return the user's info with access token (jwt)
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok(kakaoLoginDTO);
    }

    // 헤더에서 토큰을 가져와 검증한 뒤에 유효하다면 user idx를 꺼내서 유저의 정보를 반환하는 API
    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 관련 API 목록")
    @Operation(summary = "카카오 로그인 유저 조회", description = "헤더에 담긴 토큰을 검증하여 유저의 정보를 반환합니다.")
    @GetMapping("/api/v2/kakao/user")
    public ResponseEntity<User> getUserInfo (
            @Parameter(hidden = true) @Auth Long userIdx
    ) {
        User user = userService.getUser(userIdx);
        return ResponseEntity.ok(user);
    }

    // 유저의 카카오톡 친구 목록을 가져오는 API
    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 관련 API 목록")
    @Operation(summary = "카카오 친구 목록 조회", description = "유저의 카카오톡 친구 목록을 조회합니다.")
    @GetMapping("/api/v2/kakao/friends")
    public ResponseEntity<KakaoFriendDTO> getFriends(
        @RequestBody KakaoAccessToken kakaoAccessToken
    ) {
        return ResponseEntity.ok(authService.getKakaoFriends(kakaoAccessToken.getAccessToken()));
    }

}