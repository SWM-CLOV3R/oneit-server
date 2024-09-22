package clov3r.oneit_server.controller;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.repository.FriendReqRepository;
import clov3r.oneit_server.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    FriendReq friendReq = friendReqRepository.findByFromIdxAndToIdx(userIdx, friendIdx);
    if (friendReq != null) {
      return ResponseEntity.badRequest().body("이미 친구 요청을 보냈습니다.");
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



}