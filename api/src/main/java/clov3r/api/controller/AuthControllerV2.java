package clov3r.api.controller;

import clov3r.api.config.security.Auth;
import clov3r.api.config.security.TokenProvider;
import clov3r.api.domain.DTO.KakaoFriendDTO;
import clov3r.api.domain.DTO.KakaoLoginDTO;
import clov3r.api.domain.DTO.KakaoProfileDTO;
import clov3r.api.domain.DTO.NicknameCheckDTO;
import clov3r.api.domain.DTO.UserDTO;
import clov3r.api.domain.data.AuthToken;
import clov3r.api.domain.data.status.UserStatus;
import clov3r.api.domain.entity.User;
import clov3r.api.domain.request.KakaoAccessToken;
import clov3r.api.domain.request.SignupRequest;
import clov3r.api.repository.AuthRepository;
import clov3r.api.repository.UserRepository;
import clov3r.api.service.AuthService;
import clov3r.api.service.UserService;
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


    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "카카오 로그인", description = "유저의 카카오 액세스 토큰을 입력받아 자체 서비스에서 카카오 로그인을 진행합니다.")
    @PostMapping("/api/v2/kakao/login")
    public ResponseEntity<KakaoLoginDTO> kakaoLogin(@RequestBody KakaoAccessToken kakaoAccessToken) {
        KakaoProfileDTO kakaoProfileDTO = authService.getKaKaoUserInfo(kakaoAccessToken.getAccessToken());
        User user;
        boolean isSignedUp = false;
        if (userRepository.existsByEmail(kakaoProfileDTO.getKakao_account().getEmail())) {
            // 1. 이미 카카오 가입된 유저라면 status를 active로 변경
            user = userRepository.findByEmail(kakaoProfileDTO.getKakao_account().getEmail());
            if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                user.setStatus(UserStatus.ACTIVE); 
            }
            // 2. 자체 회원가입 유무 확인
            if (user.getName()!=null && user.getNickname()!=null && user.getGender()!=null && user.getBirthDate()!=null) {
                isSignedUp = true;
            }
        } else {
            user = authService.createUserByKakao(kakaoProfileDTO);
        }
        AuthToken authToken = tokenProvider.createToken(user.getIdx());
        authRepository.saveRefreshToken(authToken.getRefreshToken(), user.getEmail());

        // return the user's info with access token (jwt)
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO(isSignedUp, authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok(kakaoLoginDTO);
    }

    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "유저 닉네임 중복 검사", description = "유저의 닉네임이 중복되는지 검사합니다.")
    @GetMapping("/api/v2/nickname/check")
    public ResponseEntity<NicknameCheckDTO> checkNickname(
        @Parameter(description = "검사할 닉네임") String nickname
    ) {
        NicknameCheckDTO nicknameCheckDTO = new NicknameCheckDTO(false);
        if (userRepository.existsByNickname(nickname)) {
            nicknameCheckDTO.setExist(true);
            return ResponseEntity.ok(nicknameCheckDTO);
        }

        return ResponseEntity.ok(nicknameCheckDTO);
    }

    // 헤더에서 토큰을 가져와 검증한 뒤에 유효하다면 user idx를 꺼내서 유저의 정보를 반환하는 API
    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "카카오 로그인 유저 조회", description = "헤더에 담긴 토큰을 검증하여 유저의 정보를 반환합니다.")
    @GetMapping("/api/v2/kakao/user")
    public ResponseEntity<User> getUserInfo (
            @Parameter(hidden = true) @Auth Long userIdx
    ) {
        User user = userService.getUser(userIdx);
        return ResponseEntity.ok(user);
    }

    // 유저의 카카오톡 친구 목록을 가져오는 API
    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "카카오 친구 목록 조회", description = "유저의 카카오톡 친구 목록을 조회합니다.")
    @GetMapping("/api/v2/kakao/friends")
    public ResponseEntity<KakaoFriendDTO> getFriends(
        @RequestBody KakaoAccessToken kakaoAccessToken
    ) {
        return ResponseEntity.ok(authService.getKakaoFriends(kakaoAccessToken.getAccessToken()));
    }

    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "ONEIT 회원가입", description = "이름, 닉네임, 성별, 생년월일을 추가로 입력받습니다.")
    @PostMapping("/api/v2/signup")
    public ResponseEntity<UserDTO> signUp(
        @RequestBody SignupRequest signupRequest,
        @Parameter(hidden = true) @Auth Long userIdx
        ) {
        User user = userService.signUp(signupRequest, userIdx);
        UserDTO newUser = UserDTO.builder()
            .idx(user.getIdx())
            .email(user.getEmail())
            .name(user.getName())
            .nickname(user.getNickname())
            .profileImg(user.getProfileImg())
            .gender(user.getGender())
            .birthDate(user.getBirthDate())
            .build();
        return ResponseEntity.ok(newUser);
    }

}
