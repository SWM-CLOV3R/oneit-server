package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long userIdx) {
        return userRepository.findUser(userIdx);
    }

}
