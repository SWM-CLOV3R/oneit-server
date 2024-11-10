package clov3r.api.auth.service;

import static clov3r.api.common.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.auth.domain.dto.UserDTO;
import clov3r.api.auth.domain.request.SignupRequest;
import clov3r.api.auth.domain.request.UpdateUserRequest;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.common.service.S3Service;
import clov3r.api.friend.domain.dto.OtherUserDTO;
import clov3r.api.friend.service.FriendService;
import clov3r.api.notification.service.NotificationService;
import clov3r.domain.domains.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final FriendService friendService;
    private final NotificationService notificationService;

    public UserDTO getUser(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        return new UserDTO(user);
    }

    @Transactional
    public User signUp(SignupRequest signupRequest, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        if (user == null) {
            throw new BaseExceptionV2(USER_NOT_FOUND);
        }
        user = signupRequest.update(user);
        notificationService.sendSignupCompleteNotification(user);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void withdraw(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        user.changeInactiveUser();
    }

    @Transactional
    public User updateUser(
        UpdateUserRequest updateUserRequest,
        MultipartFile profileImage,
        Long userIdx
    ) {
        User user = userRepository.findByUserIdx(userIdx);
        // upload image if exists
        if (profileImage != null) {
            String imageUrl = updateProfileImage(profileImage);
            user.setProfileImg(imageUrl);
        }
        user = updateUserRequest.toDomain(user, profileImage);
        return user;
    }

    public String updateProfileImage(MultipartFile profileImage) {
        return s3Service.upload(profileImage, "user-profile");
    }

    @Transactional
    public void updatePhoneNumber(User user, String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
    }

    @Transactional
    public Boolean changeMarketing(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        user.setIsAgreeMarketing(!user.getIsAgreeMarketing());
        return user.getIsAgreeMarketing();
    }

    public OtherUserDTO getOtherUser(Long userIdx, Long friendIdx) {
        User user = userRepository.findByUserIdx(friendIdx);
        return new OtherUserDTO(user, friendService.isFriend(userIdx, friendIdx));
    }

}
