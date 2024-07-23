package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.DTO.KakaoLoginDTO;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.repository.KakaoRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.service.KakaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoRepository kakaoRepository;

    @Tag(name = "카카오 로그인 API", description = "카카오 로그인 API 목록")
    @GetMapping("/api/v1/kakao/login")
    public BaseResponse<KakaoLoginDTO> kakaoLogin(String kakaoAccessToken) {
        // check if the user exists
        if (kakaoRepository.existsUser(kakaoAccessToken)) {
            // if so, return the user's access token (jwt)
            User user = kakaoService.getUser(kakaoAccessToken);
            String jwt = user.getAccessToken();
            return new BaseResponse<>(new KakaoLoginDTO(user.getIdx(), user.getEmail(), jwt));
        }

        // if not, create a new user
        User user = kakaoService.createUser(kakaoAccessToken);
        // return the user's info with access token (jwt)
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO(user.getIdx(), user.getEmail(), user.getJwt());

        return new BaseResponse<>(kakaoLoginDTO);
    }

}
