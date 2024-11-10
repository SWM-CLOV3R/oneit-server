package clov3r.api.timeattack.controller;

import static clov3r.api.common.error.errorcode.CustomErrorCode.*;

import clov3r.api.auth.security.Auth;
import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.api.friend.service.FriendService;
import clov3r.api.timeattack.service.TimeAttackService;
import clov3r.domain.domains.entity.Friendship;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeAttackControllerImpl implements TimeAttackController {
  private final TimeAttackService timeAttackService;
  private final FriendService friendService;
  private final FriendshipRepository friendshipRepository;

  @Override
  public ResponseEntity<String> toggleTimeAttackAlarm(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    if (friendship == null) {
      throw new BaseExceptionV2(NOT_FOUND_FRIENDSHIP);
    }
    Boolean timeAttackAlarm = timeAttackService.toggleTimeAttackAlarm(friendship);
    return ResponseEntity.ok(userIdx + "의 친구 " + friendIdx + "의 타임어택 알람 여부 :" + timeAttackAlarm);
  }
}
