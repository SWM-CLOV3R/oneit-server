package clov3r.api.timeattack.service;

import clov3r.api.friend.repository.FriendshipRepository;
import clov3r.domain.domains.entity.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeAttackService {
  private final FriendshipRepository friendshipRepository;

  public Boolean toggleTimeAttackAlarm(Friendship friendship) {
    friendship.changeTimeAttackAlarm();
    return friendship.getTimeAttackAlarm();
  }
}
