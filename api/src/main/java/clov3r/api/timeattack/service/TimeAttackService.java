package clov3r.api.timeattack.service;

import clov3r.api.friend.domain.dto.FriendDTO;
import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.product.domain.status.LikeStatus;
import clov3r.api.product.repository.ProductLikeRepository;
import clov3r.api.product.service.ProductService;
import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.entity.User;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeAttackService {
  private final ProductLikeRepository productLikeRepository;
  private final FriendshipRepository friendshipRepository;

  public Boolean toggleTimeAttackAlarm(Friendship friendship) {
    friendship.changeTimeAttackAlarm();
    return friendship.getTimeAttackAlarm();
  }

  public List<Product> getFriendWishList(Long friendIdx) {
    return productLikeRepository.findLikeProductList(friendIdx, LikeStatus.LIKE);
  }

  public List<FriendDTO> getBirthdayFriends(Long userIdx) {
    int days = 7;
    List<User> birthdayFriends = friendshipRepository.findBirthdayFriends(userIdx, days);
    // sort by closest birthday
    birthdayFriends.sort((a, b) -> {
      int aDiff = diffDays(a.getBirthDate(), LocalDate.now());
      int bDiff = diffDays(b.getBirthDate(), LocalDate.now());
      return Integer.compare(Math.abs(aDiff), Math.abs(bDiff));
    });
    return birthdayFriends.stream()
        .map(FriendDTO::new)
        .toList();
  }

  public int diffDays(LocalDate target, LocalDate today) {
    if (target.getMonthValue() < today.getMonthValue()) {
      return target.getDayOfYear() + (today.lengthOfYear() - today.getDayOfYear());
    }
    return target.getDayOfMonth() - today.getDayOfMonth();
  }

}
