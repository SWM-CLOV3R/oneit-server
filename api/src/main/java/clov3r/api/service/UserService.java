package clov3r.api.service;

import static clov3r.api.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.domain.entity.User;
import clov3r.api.domain.request.SignupRequest;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        return user;
    }

}