package clov3r.api.timeattack.controller;

import clov3r.api.auth.security.Auth;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "타임어택 API", description = "타임어택 관련 API")
public interface TimeAttackController {


  @Operation(summary = "타임어택 알람 토글", description = "타임어택 알람을 토글합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/time-attack/toggle")
  ResponseEntity<String> toggleTimeAttackAlarm(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  );

  @Operation(summary = "친구의 위시리스트 목록 전체 조회", description = "친구의 위시리스트 목록을 전체 조회합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/wish-list")
  ResponseEntity<List<ProductSummaryDTO>> getFriendWishList(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  );

}
