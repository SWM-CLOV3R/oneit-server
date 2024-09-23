package clov3r.oneit_server.controller;

import static clov3r.oneit_server.error.errorcode.CustomErrorCode.DUPLICATE_FRIEND;
import static clov3r.oneit_server.error.errorcode.CustomErrorCode.INVALID_FRIEND_REQUEST;
import static clov3r.oneit_server.error.errorcode.CustomErrorCode.INVALID_SELF_REQUEST;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.FriendReqDTO;
import clov3r.oneit_server.domain.DTO.UserDTO;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.error.exception.BaseExceptionV2;
import clov3r.oneit_server.repository.FriendReqRepository;
import clov3r.oneit_server.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청", description = "친구 요청을 보냅니다.(서비스 가입 유저에게만 가능)")
  @PostMapping("/api/v2/friends/{friendIdx}/request")
  public ResponseEntity<String> requestFriend(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    if (userIdx.equals(friendIdx)) {
      throw new BaseExceptionV2(INVALID_SELF_REQUEST);
    }
    if (friendService.isFriend(userIdx, friendIdx)) {
      throw new BaseExceptionV2(DUPLICATE_FRIEND);
    }
    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(userIdx, friendIdx);
    if (friendReq != null) {
      throw new BaseExceptionV2(INVALID_FRIEND_REQUEST);
    }
    friendService.requestFriend(userIdx, friendIdx);
    return ResponseEntity.ok("From: " + userIdx + " -> To: " + friendIdx + " 친구 요청 성공");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/accept")
  public ResponseEntity<String> acceptFriend(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    friendService.acceptFriend(userIdx, friendIdx);
    if (!friendService.isFriend(userIdx, friendIdx)) {
      friendService.createNewFriendship(userIdx, friendIdx);
    }
    return ResponseEntity.ok("친구 요청 수락");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 거절", description = "친구 요청을 거절합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/reject")
  public ResponseEntity<String> rejectFriend(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    friendService.rejectFriend(userIdx, friendIdx);
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
  @DeleteMapping("/api/v2/friends/{friendIdx}/request")
  public ResponseEntity<String> cancelFriendRequest(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    friendService.cancelFriendRequest(userIdx, friendIdx);
    return ResponseEntity.ok("친구 요청 취소");
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 요청 목록 확인", description = "나에게 친구를 요청한 목록을 확인합니다.")
  @GetMapping("/api/v2/friends/requests")
  public ResponseEntity<List<FriendReqDTO>> getFriendRequests(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<FriendReqDTO> friendReqDTOList = friendService.getFriendRequests(userIdx);
    return ResponseEntity.ok(friendReqDTOList);
  }

  @Tag(name = "친구관리 API", description = "친구 관련 API")
  @Operation(summary = "친구 목록 확인", description = "나의 친구 목록을 확인합니다.")
  @GetMapping("/api/v2/friends")
  public ResponseEntity<List<UserDTO>> getFriends(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<UserDTO> friends = friendService.getFriends(userIdx);
    return ResponseEntity.ok(friends);
  }

}