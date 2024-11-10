package clov3r.api.timeattack.controller;

import clov3r.api.auth.security.Auth;
import clov3r.api.timeattack.service.TimeAttackService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeAttackControllerImpl implements TimeAttackController {
  private final TimeAttackService timeAttackService;

  @Override
  public ResponseEntity<String> toggleTimeAttackAlarm(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Boolean timeAttackAlarm = timeAttackService.toggleTimeAttackAlarm(friendIdx, userIdx);
    return ResponseEntity.ok(userIdx + "의 친구 " + friendIdx + "의 타임어택 알람 여부 :" + timeAttackAlarm);
  }
}
