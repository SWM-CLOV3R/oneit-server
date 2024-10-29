package clov3r.api.auth.service;

import static clov3r.api.common.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.auth.domain.entity.User;
import clov3r.api.auth.domain.request.SignupRequest;
import clov3r.api.auth.domain.request.UpdateUserRequest;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.common.service.S3Service;
import clov3r.api.notification.service.NotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final S3Service s3Service;

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

    @Transactional
    public User updateUser(
        UpdateUserRequest updateUserRequest,
        MultipartFile profileImage,
        Long userIdx
    ) {
        User user = userRepository.findByUserIdx(userIdx);
        user.setNickname(updateUserRequest.getNickName());
        user.setBirthDate(updateUserRequest.getBirthDate());

        // upload image if exists
        if (profileImage != null) {
            String imageUrl = updateProfileImage(profileImage, userIdx);
        }
        return user;
    }

    public String updateProfileImage(MultipartFile profileImage, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        String imageUrl = s3Service.upload(profileImage, "user-profile");
        user.setProfileImg(imageUrl);
        return imageUrl;
    }

}
