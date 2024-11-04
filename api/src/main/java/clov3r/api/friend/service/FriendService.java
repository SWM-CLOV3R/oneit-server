package clov3r.api.friend.service;

import clov3r.api.friend.domain.dto.FriendDTO;
import clov3r.api.friend.domain.dto.FriendReqDTO;
import clov3r.api.friend.domain.data.FriendReqStatus;
import clov3r.api.common.domain.status.Status;
import clov3r.api.friend.domain.entity.FriendReq;
import clov3r.api.friend.domain.entity.Friendship;
import clov3r.api.friend.repository.FriendReqRepository;
import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

  private final FriendReqRepository friendReqRepository;
  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;
  private final NotificationService notificationService;


  public FriendReq requestFriend(Long userIdx, Long friendIdx) {

    FriendReq friendReq = new FriendReq(
        userRepository.findByUserIdx(userIdx),
        userRepository.findByUserIdx(friendIdx)
    );
    friendReqRepository.save(friendReq);

    // Send notification
    notificationService.sendFriendRequestNotification(friendReq);

    return friendReq;
  }

  @Transactional
  public void acceptFriend(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.accept();

    // Send notification
    notificationService.sendFriendAcceptanceNotification(friendReq);

  }

  @Transactional
  public void createNewFriendship(Long userIdx, Long friendIdx) {
    Friendship friendshipA = new Friendship(
        userRepository.findByUserIdx(userIdx),
        userRepository.findByUserIdx(friendIdx)
    );
    Friendship friendshipB = new Friendship(
        userRepository.findByUserIdx(friendIdx),
        userRepository.findByUserIdx(userIdx)
    );
    friendshipRepository.save(friendshipA);
    friendshipRepository.save(friendshipB);
}

  @Transactional
  public void rejectFriend(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.reject();
  }

  @Transactional
  public void deleteFriend(Long userIdx, Long friendIdx) {
    Friendship friendshipA = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    Friendship friendshipB = friendshipRepository.findByUserIdxAndFriendIdx(friendIdx, userIdx);
    friendshipA.deleteFriendship();
    friendshipB.deleteFriendship();
  }

  public boolean isFriend(Long userIdx, Long friendIdx) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    return friendship != null;
  }

  @Transactional
  public void cancelFriendRequest(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.cancel();
  }

  public List<FriendReqDTO> getFriendRequestsToMe(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByToIdx(userIdx);
    return friendReqs.stream().map(friendReq -> {
      FriendDTO fromUser = new FriendDTO(friendReq.getFrom());
      return new FriendReqDTO(friendReq.getIdx(), fromUser, friendReq.getCreatedAt());
    }).toList();
  }

  public List<FriendReqDTO> getFriendRequestsFromMe(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByFromIdx(userIdx);
    return friendReqs.stream().map(friendReq -> {
      FriendDTO ToUser = new FriendDTO(friendReq.getTo());
      return new FriendReqDTO(friendReq.getIdx(), ToUser, friendReq.getCreatedAt());
    }).toList();
  }


  public List<FriendDTO> getFriends(Long userIdx) {
    List<Friendship> friendshipsList = friendshipRepository.findByUserIdx(userIdx);
    return friendshipsList.stream()
        .map(friendship -> new FriendDTO(friendship.getFriend()))
        .toList();
  }
}
