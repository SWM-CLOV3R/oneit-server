package clov3r.api.auth.controller;

import static clov3r.api.common.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.auth.domain.request.UpdateUserRequest;
import clov3r.api.auth.config.security.Auth;
import clov3r.api.auth.config.security.TokenProvider;
import clov3r.api.auth.domain.dto.KakaoFriendDTO;
import clov3r.api.auth.domain.dto.KakaoLoginDTO;
import clov3r.api.auth.domain.dto.KakaoProfileDTO;
import clov3r.api.auth.domain.dto.NicknameCheckDTO;
import clov3r.api.auth.domain.dto.UserDTO;
import clov3r.api.auth.domain.data.AuthToken;
import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.auth.domain.entity.User;
import clov3r.api.auth.domain.request.KakaoAccessTokenRequest;
import clov3r.api.auth.domain.request.SignupRequest;
import clov3r.api.auth.repository.AuthRepository;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.auth.service.AuthService;
import clov3r.api.auth.service.UserService;
import clov3r.api.common.error.exception.BaseExceptionV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;


    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "카카오 로그인", description = "유저의 카카오 액세스 토큰을 입력받아 자체 서비스에서 카카오 로그인을 진행합니다.")
    @PostMapping("/api/v2/kakao/login")
    public ResponseEntity<KakaoLoginDTO> kakaoLogin(@RequestBody KakaoAccessTokenRequest kakaoAccessTokenRequest) {
        KakaoProfileDTO kakaoProfileDTO = authService.getKaKaoUserInfo(kakaoAccessTokenRequest.getAccessToken());

        // 카카오 프로필 정보 중 "이메일"로 유저 조회
        User user = userRepository.findByEmail(kakaoProfileDTO.getKakao_account().getEmail());

        boolean isSignedUp = false;
        if (user == null) {
            // 최초 회원가입
            user = authService.createUserByKakao(kakaoProfileDTO);
        } else if (user.getStatus().equals(UserStatus.INACTIVE)) {
            // 탈퇴한 유저, 탈퇴후 7일이 지나기 전이라면 status를 active로 변경
            user.backToActiveUser();
        } else if (user.getStatus().equals(UserStatus.DELETED)) {
            // 탈퇴한 유저, 탈퇴후 7일이 지난 경우 다시 회원가입 진행
            user = authService.createUserByKakao(kakaoProfileDTO);
        } else {
            // 1. 자체 회원가입 필수 정보(이름, 닉네임, 성별, 생일, 전화번호)가 있는지 확인
            if (user.getName()!=null && user.getNickname()!=null && user.getGender()!=null && user.getBirthDate()!=null && user.getPhoneNumber()!=null) {
                isSignedUp = true;
            }
            // 2. 카카오 프로필 정보로 업데이트
            if (user.getPhoneNumber() == null) {
                userService.updatePhoneNumber(user, kakaoProfileDTO.getKakao_account().phone_number);
            }
        }

        AuthToken authToken = tokenProvider.createToken(user.getIdx());
        authRepository.saveRefreshToken(authToken.getRefreshToken(), user.getEmail());

        // return the user's info with access token (jwt)
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO(
            isSignedUp,
            authToken.getAccessToken(),
            authToken.getRefreshToken()
        );
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
    public ResponseEntity<UserDTO> getUserInfo (
            @Parameter(hidden = true) @Auth Long userIdx
    ) {
        if (userRepository.findByUserIdx(userIdx) == null) {
            throw new BaseExceptionV2(USER_NOT_FOUND);
        }
        return ResponseEntity.ok(userService.getUser(userIdx));
    }

    // 유저의 카카오톡 친구 목록을 가져오는 API
    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "카카오 친구 목록 조회", description = "유저의 카카오톡 친구 목록을 조회합니다.")
    @GetMapping("/api/v2/kakao/friends")
    public ResponseEntity<KakaoFriendDTO> getFriends(
        @RequestBody KakaoAccessTokenRequest kakaoAccessTokenRequest
    ) {
        return ResponseEntity.ok(authService.getKakaoFriends(kakaoAccessTokenRequest.getAccessToken()));
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
            .isAgreeMarketing(user.getIsAgreeMarketing())
            .build();
        return ResponseEntity.ok(newUser);
    }

    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "회원정보 수정", description = "회원정보를 수정합니다.")
    @PatchMapping(value = "/api/v2/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUser(
        @RequestPart UpdateUserRequest updateUserRequest,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @Parameter(hidden = true) @Auth Long userIdx
    ) {
        User user = userService.updateUser(updateUserRequest, profileImage, userIdx);
        UserDTO updatedUser = UserDTO.builder()
            .idx(user.getIdx())
            .email(user.getEmail())
            .name(user.getName())
            .nickname(user.getNickname())
            .profileImg(user.getProfileImg())
            .gender(user.getGender())
            .birthDate(user.getBirthDate())
            .build();
        return ResponseEntity.ok(updatedUser);
    }

    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @PostMapping("/api/v2/withdraw")
    public ResponseEntity<String> withdraw(
        @Parameter(hidden = true) @Auth Long userIdx
    ) {
        userService.withdraw(userIdx);
        return ResponseEntity.ok("탈퇴가 완료되었습니다.");
    }

    @Tag(name = "계정 API", description = "회원가입/로그인 관련 API 목록")
    @Operation(summary = "마케팅 수신 동의 변경", description = "마케팅 수신 동의 여부를 변경합니다.")
    @PatchMapping("/api/v2/marketing")
    public ResponseEntity<String> changeMarketing(
        @Parameter(hidden = true) @Auth Long userIdx
    ) {
        Boolean isAgreeMarketing = userService.changeMarketing(userIdx);
        return ResponseEntity.ok("마케팅 수신 동의 : " + isAgreeMarketing);
    }

}
