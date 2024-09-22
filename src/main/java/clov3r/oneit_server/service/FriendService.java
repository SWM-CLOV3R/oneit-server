package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.status.FriendReqStatus;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.domain.entity.Friendship;
import clov3r.oneit_server.repository.FriendReqRepository;
import clov3r.oneit_server.repository.FriendshipRepository;
import clov3r.oneit_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendReqRepository friendReqRepository;
  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;

  public void requestFriend(Long userIdx, Long friendIdx) {

    FriendReq friendReq = FriendReq.builder()
        .from(userRepository.findById(userIdx))
        .to(userRepository.findById(friendIdx))
        .friendReqStatus(FriendReqStatus.REQUESTED)
        .build();
    friendReq.createBaseEntity();
    friendReqRepository.save(friendReq);
  }

  @Transactional
  public void acceptFriend(Long userIdx, Long friendIdx) {

    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(friendIdx, userIdx);
    friendReq.accept();
    friendReq.updateBaseEntity();
  }

  @Transactional
  public void createNewFriendship(Long userIdx, Long friendIdx) {
    Friendship friendshipA = Friendship.builder()
        .user(userRepository.findById(userIdx))
        .friend(userRepository.findById(friendIdx))
        .build();
    Friendship friendshipB = Friendship.builder()
        .user(userRepository.findById(friendIdx))
        .friend(userRepository.findById(userIdx))
        .build();
    friendshipA.createBaseEntity();
    friendshipB.createBaseEntity();
    friendshipRepository.save(friendshipA);
    friendshipRepository.save(friendshipB);
  }

  @Transactional
  public void rejectFriend(Long userIdx, Long friendIdx) {

    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(friendIdx, userIdx);
    friendReq.reject();
    friendReq.updateBaseEntity();
  }
}
