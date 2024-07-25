package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.DTO.AuthToken;
import clov3r.oneit_server.domain.DTO.KakaoLoginDTO;
import clov3r.oneit_server.domain.DTO.KakaoProfile;
import clov3r.oneit_server.domain.collectioin.KakaoAccessToken;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.repository.KakaoRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.service.KakaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoRepository kakaoRepository;

    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 API 목록")
    @PostMapping("/api/v1/kakao/login")
    public BaseResponse<KakaoLoginDTO> kakaoLogin(@RequestBody KakaoAccessToken kakaoAccessToken) {
        // get user info from kakao api
        KakaoProfile kakaoProfile = kakaoService.getUserInfo(kakaoAccessToken.getAccessToken());
        User user = new User();

        // check if the user exists
        if (kakaoRepository.existsUser(kakaoProfile.getKakao_account().getEmail())) {
            // if so, return the user's access token (jwt)
            user = kakaoService.getUser(kakaoProfile.getKakao_account().getEmail());
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



}
