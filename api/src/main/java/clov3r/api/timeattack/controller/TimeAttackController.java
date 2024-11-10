package clov3r.api.timeattack.controller;

import clov3r.api.auth.security.Auth;
import clov3r.api.friend.domain.dto.FriendDTO;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "타임어택 API", description = "타임어택 관련 API")
public interface TimeAttackController {


  @Operation(summary = "타임어택 알람 토글", description = "타임어택 알람을 토글합니다.")
  @PostMapping("/api/v2/friends/{friendIdx}/time-attack/toggle")
  ResponseEntity<String> toggleTimeAttackAlarm(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  );

  @Operation(summary = "친구의 위시리스트 목록 전체 조회", description = "친구의 위시리스트 목록을 전체 조회합니다.")
  @GetMapping("/api/v2/friends/{friendIdx}/wish-list")
  ResponseEntity<List<ProductSummaryDTO>> getFriendWishList(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  );

  @Operation(summary = "친구의 위시리스트 랜덤제품 조회", description = "친구의 위시리스트 목록에서 제품을 랜덤으로 조회합니다.")
  @GetMapping("/api/v2/friends/{friendIdx}/wish-list/random")
  ResponseEntity<ProductSummaryDTO> getFriendWishListRandomProduct(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx,
      @Parameter(description = "제외할 제품 ID") @RequestParam(required = false) Long excludeProductId
  );

  @Operation(summary = "7일 이내 생일인 친구 조회", description = "7일 이내 생일인 친구를 조회합니다.")
  @GetMapping("/api/v2/friends/birthday")
  ResponseEntity<List<FriendDTO>> getBirthdayFriends(
      @Parameter(hidden = true) @Auth Long userIdx
  );
}
