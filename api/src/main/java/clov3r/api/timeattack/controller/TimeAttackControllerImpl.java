package clov3r.api.timeattack.controller;

import static clov3r.api.common.error.errorcode.CustomErrorCode.*;

import clov3r.api.auth.security.Auth;
import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.friend.domain.dto.FriendDTO;
import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.api.friend.service.FriendService;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.product.service.ProductService;
import clov3r.api.timeattack.dto.BirthdayFriendDTO;
import clov3r.api.timeattack.service.TimeAttackService;
import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.Product;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeAttackControllerImpl implements TimeAttackController {

  private final TimeAttackService timeAttackService;
  private final FriendshipRepository friendshipRepository;
  private final ProductService productService;

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

  @Override
  public ResponseEntity<List<ProductSummaryDTO>> getFriendWishList(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    if (friendship == null) {
      throw new BaseExceptionV2(NOT_FOUND_FRIENDSHIP);
    }
    if (!friendship.getTimeAttackAlarm()) {
      throw new BaseExceptionV2(TIME_ATTACK_ALARM_OFF);
    }
    List<Product> friendWishList = timeAttackService.getFriendWishList(friendIdx);
    List<ProductSummaryDTO> productSummaryDTOList = friendWishList.stream()
        .map(product -> new ProductSummaryDTO(
            product,
            productService.getLikeStatus(product.getIdx(), userIdx)
        )).toList();
    return ResponseEntity.ok(productSummaryDTOList);
  }

  @Override
  public ResponseEntity<ProductSummaryDTO> getFriendWishListRandomProduct(
      @PathVariable Long friendIdx,
      @Parameter(hidden = true) @Auth Long userIdx,
      @Parameter(description = "제외할 제품 IDX") Long excludeProductIdx
  ) {
    Friendship friendship = friendshipRepository.findByUserIdxAndFriendIdx(userIdx, friendIdx);
    if (friendship == null) {
      throw new BaseExceptionV2(NOT_FOUND_FRIENDSHIP);
    }
    if (!friendship.getTimeAttackAlarm()) {
      throw new BaseExceptionV2(TIME_ATTACK_ALARM_OFF);
    }
    List<Product> friendWishList = timeAttackService.getFriendWishList(friendIdx);
    if (friendWishList.size() == 1 && Objects.equals(excludeProductIdx,
        friendWishList.get(0).getIdx())) {
      throw new BaseExceptionV2(CustomErrorCode.NO_MORE_PRODUCTS);
    }
    // Get random product from friend's wish list
    Product randomProduct = productService.getRandomProduct(friendWishList, excludeProductIdx);
    ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO(
        randomProduct,
        productService.getLikeStatus(randomProduct.getIdx(), userIdx)
    );
    return ResponseEntity.ok(productSummaryDTO);
  }

  @Override
  public ResponseEntity<List<BirthdayFriendDTO>> getBirthdayFriends(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<BirthdayFriendDTO> birthdayFriends = timeAttackService.getBirthdayFriends(userIdx);
    return ResponseEntity.ok(birthdayFriends);
  }
}
