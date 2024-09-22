package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.DTO.FriendReqDTO;
import clov3r.oneit_server.domain.DTO.UserDTO;
import clov3r.oneit_server.domain.data.status.FriendReqStatus;
import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.domain.entity.Friendship;
import clov3r.oneit_server.repository.FriendReqRepository;
import clov3r.oneit_server.repository.FriendshipRepository;
import clov3r.oneit_server.repository.UserRepository;
import java.util.List;
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
    friendshipA.setStatus(Status.ACTIVE);
    friendshipB.setStatus(Status.ACTIVE);
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

  @Transactional
  public void deleteFriend(Long userIdx, Long friendIdx) {
    Friendship friendshipA = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    Friendship friendshipB = friendshipRepository.findByUserIdxAndFriendIdx(friendIdx, userIdx);
    friendshipA.setStatus(Status.DELETED);
    friendshipB.setStatus(Status.DELETED);
    friendshipA.deleteBaseEntity();
    friendshipB.deleteBaseEntity();
  }

  public boolean isFriend(Long userIdx, Long friendIdx) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    return friendship != null;
  }

  @Transactional
  public void cancelFriendRequest(Long userIdx, Long friendIdx) {
    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(userIdx, friendIdx);
    friendReqRepository.delete(friendReq);
  }

  public List<FriendReqDTO> getFriendRequests(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByToIdx(userIdx);
    List<FriendReqDTO> friendReqDTOList = friendReqs.stream().map(friendReq -> {
      UserDTO fromUser = UserDTO.builder()
          .idx(friendReq.getFrom().getIdx())
          .name(friendReq.getFrom().getName())
          .nickName(friendReq.getFrom().getNickname())
          .profileImg(friendReq.getFrom().getProfileImgFromKakao())
          .birthDate(friendReq.getFrom().getBirthDate())
          .build();
      System.out.println("friendReq.getCreatedAt().getClass().getName() = " + friendReq.getCreatedAt().getClass().getName());
      return new FriendReqDTO(fromUser, friendReq.getCreatedAt());
    }).toList();
    return friendReqDTOList;
  }

  public List<UserDTO> getFriends(Long userIdx) {
    List<Friendship> friendshipsList = friendshipRepository.findByUserIdx(userIdx);
    return friendshipsList.stream()
        .map(friendship -> UserDTO.builder()
            .idx(friendship.getFriend().getIdx())
            .name(friendship.getFriend().getName())
            .nickName(friendship.getFriend().getNickname())
            .profileImg(friendship.getFriend().getProfileImgFromKakao())
            .birthDate(friendship.getFriend().getBirthDate())
            .build())
        .toList();
  }

}
