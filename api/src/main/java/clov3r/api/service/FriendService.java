package clov3r.api.service;

import clov3r.api.domain.DTO.FriendDTO;
import clov3r.api.domain.DTO.FriendReqDTO;
import clov3r.api.domain.data.status.FriendReqStatus;
import clov3r.api.domain.data.status.Status;
import clov3r.api.domain.entity.FriendReq;
import clov3r.api.domain.entity.Friendship;
import clov3r.api.domain.entity.Notification;
import clov3r.api.repository.FriendReqRepository;
import clov3r.api.repository.FriendshipRepository;
import clov3r.api.repository.NotificationRepository;
import clov3r.api.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
  private final ApplicationEventPublisher applicationEventPublisher;
  private final NotificationRepository notificationRepository;

  public FriendReq requestFriend(Long userIdx, Long friendIdx) {

    FriendReq friendReq = FriendReq.builder()
        .from(userRepository.findByUserIdx(userIdx))
        .to(userRepository.findByUserIdx(friendIdx))
        .friendReqStatus(FriendReqStatus.REQUESTED)
        .build();
    friendReq.createBaseEntity();
    friendReqRepository.save(friendReq);

    // Send notification
    Notification notification = notificationService.sendFriendRequestNotification(friendReq);
    applicationEventPublisher.publishEvent(notification);
    notificationRepository.save(notification);
    return friendReq;
  }

  @Transactional
  public void acceptFriend(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.accept();
    friendReq.updateBaseEntity();

    // Send notification
    Notification notification = notificationService.sendFriendAcceptanceNotification(friendReq);
    applicationEventPublisher.publishEvent(notification);
    notificationRepository.save(notification);
  }

  @Transactional
  public void createNewFriendship(Long userIdx, Long friendIdx) {
    Friendship friendshipA = Friendship.builder()
        .user(userRepository.findByUserIdx(userIdx))
        .friend(userRepository.findByUserIdx(friendIdx))
        .build();
    Friendship friendshipB = Friendship.builder()
        .user(userRepository.findByUserIdx(friendIdx))
        .friend(userRepository.findByUserIdx(userIdx))
        .build();
    friendshipA.setStatus(Status.ACTIVE);
    friendshipB.setStatus(Status.ACTIVE);
    friendshipA.createBaseEntity();
    friendshipB.createBaseEntity();
    friendshipRepository.save(friendshipA);
    friendshipRepository.save(friendshipB);
}

  @Transactional
  public void rejectFriend(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.reject();
    friendReq.updateBaseEntity();
  }

  @Transactional
  public void deleteFriend(Long userIdx, Long friendIdx) {
    Friendship friendshipA = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    Friendship friendshipB = friendshipRepository.findByUserIdxAndFriendIdx(friendIdx, userIdx);
    friendshipRepository.delete(friendshipA);
    friendshipRepository.delete(friendshipB);
  }

  public boolean isFriend(Long userIdx, Long friendIdx) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    return friendship != null;
  }

  @Transactional
  public void cancelFriendRequest(Long requestIdx) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReqRepository.delete(friendReq);
  }

  public List<FriendReqDTO> getFriendRequestsToMe(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByToIdx(userIdx);
    return friendReqs.stream().map(friendReq -> {
      FriendDTO fromUser = FriendDTO.builder()
          .idx(friendReq.getFrom().getIdx())
          .name(friendReq.getFrom().getName())
          .nickName(friendReq.getFrom().getNickname())
          .profileImg(friendReq.getFrom().getProfileImg())
          .birthDate(friendReq.getFrom().getBirthDate())
          .build();
      return new FriendReqDTO(friendReq.getIdx(), fromUser, friendReq.getCreatedAt());
    }).toList();
  }

  public List<FriendReqDTO> getFriendRequestsFromMe(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByFromIdx(userIdx);
    return friendReqs.stream().map(friendReq -> {
      FriendDTO ToUser = FriendDTO.builder()
          .idx(friendReq.getTo().getIdx())
          .name(friendReq.getTo().getName())
          .nickName(friendReq.getTo().getNickname())
          .profileImg(friendReq.getTo().getProfileImg())
          .birthDate(friendReq.getTo().getBirthDate())
          .build();
      return new FriendReqDTO(friendReq.getIdx(), ToUser, friendReq.getCreatedAt());
    }).toList();
  }


  public List<FriendDTO> getFriends(Long userIdx) {
    List<Friendship> friendshipsList = friendshipRepository.findByUserIdx(userIdx);
    return friendshipsList.stream()
        .map(friendship -> FriendDTO.builder()
            .idx(friendship.getFriend().getIdx())
            .name(friendship.getFriend().getName())
            .nickName(friendship.getFriend().getNickname())
            .profileImg(friendship.getFriend().getProfileImg())
            .birthDate(friendship.getFriend().getBirthDate())
            .build())
        .toList();
  }

}
