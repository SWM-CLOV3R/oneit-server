package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.DTO.FriendReqDTO;
import clov3r.oneit_server.domain.DTO.FriendDTO;
import clov3r.oneit_server.domain.data.status.FriendReqStatus;
import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.domain.entity.Friendship;
import clov3r.oneit_server.domain.entity.Notification;
import clov3r.oneit_server.repository.DeviceRepository;
import clov3r.oneit_server.repository.FriendReqRepository;
import clov3r.oneit_server.repository.FriendshipRepository;
import clov3r.oneit_server.repository.UserRepository;
import java.io.IOException;
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
  private final NotificationService notificationService;
  private final FCMService fcmService;
  private final DeviceRepository deviceRepository;

  public FriendReq requestFriend(Long userIdx, Long friendIdx) throws IOException {

    FriendReq friendReq = FriendReq.builder()
        .from(userRepository.findByUserIdx(userIdx))
        .to(userRepository.findByUserIdx(friendIdx))
        .friendReqStatus(FriendReqStatus.REQUESTED)
        .build();
    friendReq.createBaseEntity();
    friendReqRepository.save(friendReq);
    Notification notification = notificationService.sendFreindRequestNotification(friendReq);
    String deviceToken = deviceRepository.findByUserId(notification.getReceiver().getIdx()).getDeviceToken();
    fcmService.sendMessageTo(deviceToken, "친구 요청", notification.getSender().getNickname() + "님이 친구 요청을 보냈습니다.");

    return friendReq;
  }

  @Transactional
  public void acceptFriend(Long requestIdx) throws IOException {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    friendReq.accept();
    friendReq.updateBaseEntity();
    Notification notification = notificationService.sendFriendAcceptanceNotification(friendReq);
    String deviceToken = deviceRepository.findByUserId(notification.getReceiver().getIdx()).getDeviceToken();
    fcmService.sendMessageTo(deviceToken, "친구 요청 수락", notification.getSender().getNickname() + "님이 친구 요청을 수락했습니다.");

  }

  @Transactional
  public void createNewFriendship(Long userIdx, Long friendIdx) throws IOException {
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
    List<FriendReqDTO> friendReqDTOList = friendReqs.stream().map(friendReq -> {
      FriendDTO fromUser = FriendDTO.builder()
          .idx(friendReq.getFrom().getIdx())
          .name(friendReq.getFrom().getName())
          .nickName(friendReq.getFrom().getNickname())
          .profileImg(friendReq.getFrom().getProfileImg())
          .birthDate(friendReq.getFrom().getBirthDate())
          .build();
      return new FriendReqDTO(friendReq.getIdx(), fromUser, friendReq.getCreatedAt());
    }).toList();
    return friendReqDTOList;
  }

  public List<FriendReqDTO> getFriendRequestsFromMe(Long userIdx) {
    List<FriendReq> friendReqs = friendReqRepository.findAllByFromIdx(userIdx);
    List<FriendReqDTO> friendReqDTOList = friendReqs.stream().map(friendReq -> {
      FriendDTO ToUser = FriendDTO.builder()
          .idx(friendReq.getTo().getIdx())
          .name(friendReq.getTo().getName())
          .nickName(friendReq.getTo().getNickname())
          .profileImg(friendReq.getTo().getProfileImg())
          .birthDate(friendReq.getTo().getBirthDate())
          .build();
      return new FriendReqDTO(friendReq.getIdx(), ToUser, friendReq.getCreatedAt());
    }).toList();
    return friendReqDTOList;
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
