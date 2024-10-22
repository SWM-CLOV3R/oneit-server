package clov3r.api.service;

import static clov3r.api.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.domain.DTO.KakaoProfileDTO;
import clov3r.api.domain.DTO.kakao.KakaoAlarmResponseDTO;
import clov3r.api.domain.DTO.kakao.KakaoButton;
import clov3r.api.domain.data.kakao.SIGNUP_COMPLETE;
import clov3r.api.domain.data.status.Status;
import clov3r.api.domain.data.status.UserStatus;
import clov3r.api.domain.entity.Notification;
import clov3r.api.domain.entity.User;
import clov3r.api.domain.request.SignupRequest;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public User getUser(Long userIdx) {
        return userRepository.findByUserIdx(userIdx);
    }

    @Transactional
    public User signUp(SignupRequest signupRequest, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        if (user == null) {
            throw new BaseExceptionV2(USER_NOT_FOUND);
        }
        user.setName(signupRequest.getName());
        user.setNickname(signupRequest.getNickname());
        user.setGender(signupRequest.getGender());
        user.setBirthDate(signupRequest.getBirthDate());
        userRepository.save(user);

//        notificationService.sendSignupCompleteNotification(user);
        return user;
    }

    @Transactional
    public void withdraw(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
    }

}
