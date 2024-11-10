package clov3r.api.timeattack.service;

import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.product.domain.status.LikeStatus;
import clov3r.api.product.repository.ProductLikeRepository;
import clov3r.api.product.service.ProductService;
import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeAttackService {
  private final ProductLikeRepository productLikeRepository;

  public Boolean toggleTimeAttackAlarm(Friendship friendship) {
    friendship.changeTimeAttackAlarm();
    return friendship.getTimeAttackAlarm();
  }

  public List<Product> getFriendWishList(Long friendIdx) {
    return productLikeRepository.findLikeProductList(friendIdx, LikeStatus.LIKE);
  }
}
