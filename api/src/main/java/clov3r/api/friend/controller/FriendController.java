package clov3r.api.friend.controller;

import clov3r.api.auth.service.UserService;
import clov3r.api.friend.domain.dto.OtherUserDTO;
import clov3r.api.friend.domain.dto.RequestFriendDTO;
import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.auth.security.Auth;
import clov3r.api.friend.domain.dto.FriendDTO;
import clov3r.api.friend.domain.dto.FriendReqDTO;
import clov3r.api.friend.repository.FriendReqRepository;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.friend.service.FriendService;
import clov3r.domain.domains.entity.FriendReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;
  private final FriendReqRepository friendReqRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청", description = "친구 요청을 보냅니다.(서비스 가입 유저에게만 가능)")
  @PostMapping("/api/v2/friends/{friendIdx}/request")
  public ResponseEntity<RequestFriendDTO> requestFriend(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) throws IOException {
    if (userIdx.equals(friendIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.INVALID_SELF_REQUEST);
    }
    if (!userRepository.existsByUserIdx(friendIdx) || !userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.USER_NOT_FOUND);
    }
    if (friendService.isFriend(userIdx, friendIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.DUPLICATE_FRIEND);
    }
    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(userIdx, friendIdx);
    if (friendReq != null) {
      throw new BaseExceptionV2(CustomErrorCode.INVALID_FRIEND_REQUEST);
    }
    FriendReq newReq = friendService.requestFriend(userIdx, friendIdx);
    return ResponseEntity.ok(new RequestFriendDTO(newReq.getIdx(), userIdx, friendIdx, newReq.getCreatedAt()));
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/request/{requestIdx}/acceptance")
  public ResponseEntity<String> acceptFriend(
      @PathVariable Long friendIdx,
      @PathVariable Long requestIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) throws IOException {
    if (!userRepository.existsByUserIdx(friendIdx) || !userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.USER_NOT_FOUND);
    }
    friendService.acceptFriend(requestIdx);
    if (!friendService.isFriend(userIdx, friendIdx)) {
      friendService.createNewFriendship(userIdx, friendIdx);
    }
    return ResponseEntity.ok("친구 요청 수락");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 거절", description = "친구 요청을 거절합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/request/{requestIdx}/rejection")
  public ResponseEntity<String> rejectFriend(
      @PathVariable Long friendIdx,
      @PathVariable Long requestIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    if (!friendReq.getFrom().getIdx().equals(friendIdx) || !friendReq.getTo().getIdx().equals(userIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.INVALID_FRIEND_REQUEST);
    }
    friendService.rejectFriend(requestIdx);
    return ResponseEntity.ok("친구 요청 거절");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 삭제", description = "친구 관계를 삭제합니다.")
  @DeleteMapping("/api/v2/friends/{friendIdx}")
  public ResponseEntity<String> deleteFriend(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    friendService.deleteFriend(userIdx, friendIdx);
    return ResponseEntity.ok("친구 삭제");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 취소", description = "친구 요청을 취소합니다.")
  @DeleteMapping("/api/v2/friends/{friendIdx}/request/{requestIdx}/cancel")
  public ResponseEntity<String> cancelFriendRequest(
      @PathVariable Long requestIdx,
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    FriendReq friendReq = friendReqRepository.findByIdx(requestIdx);
    if (!friendReq.getFrom().getIdx().equals(userIdx) || !friendReq.getTo().getIdx().equals(friendIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.INVALID_FRIEND_REQUEST);
    }
    friendService.cancelFriendRequest(requestIdx);
    return ResponseEntity.ok("친구 요청 취소");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 목록 확인", description = "나에게 친구를 요청한 목록을 확인합니다.")
  @GetMapping("/api/v2/friends/requests-to-me")
  public ResponseEntity<List<FriendReqDTO>> getFriendRequestsToMe(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<FriendReqDTO> friendReqDTOList = friendService.getFriendRequestsToMe(userIdx);
    return ResponseEntity.ok(friendReqDTOList);
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "내가 요청한 친구 요청 목록 확인", description = "내가 요청한 친구요청 목록을 확인합니다.")
  @GetMapping("/api/v2/friends/requests-from-me")
  public ResponseEntity<List<FriendReqDTO>> getFriendRequestsFromMe(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<FriendReqDTO> friendReqDTOList = friendService.getFriendRequestsFromMe(userIdx);
    return ResponseEntity.ok(friendReqDTOList);
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 목록 확인", description = "나의 친구 목록을 확인합니다.")
  @GetMapping("/api/v2/friends")
  public ResponseEntity<List<FriendDTO>> getFriends(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<FriendDTO> friends = friendService.getFriends(userIdx);
    return ResponseEntity.ok(friends);
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "다른 유저 프로필 확인", description = "친구 혹은 다른 유저의 프로필을 조회합니다.")
  @GetMapping("/api/v2/friends/{friendIdx}")
  public ResponseEntity<OtherUserDTO> getFriendProfile(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    OtherUserDTO otherUserDTO = userService.getOtherUser(userIdx, friendIdx);
    return ResponseEntity.ok(otherUserDTO);
  }

}