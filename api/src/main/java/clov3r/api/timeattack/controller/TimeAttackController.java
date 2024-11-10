package clov3r.api.timeattack.controller;

import clov3r.api.auth.security.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface TimeAttackController {

  @Tag(name = "타임어택 API", description = "타임어택 관련 API")
  @Operation(summary = "타임어택 알람 토글", description = "타임어택 알람을 토글합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/time-attack/toggle")
  ResponseEntity<String> toggleTimeAttackAlarm(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  );

}
