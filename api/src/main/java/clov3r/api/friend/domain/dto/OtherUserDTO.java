package clov3r.api.friend.domain.dto;

import clov3r.domain.domains.entity.User;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OtherUserDTO {
  private Long idx;
  private String nickName;
  private String profileImg;
  private LocalDate birthDate;
  private Boolean isFriend;
  @Setter
  private boolean timeAttackAlarm;

  public OtherUserDTO(User user, Boolean isFriend) {
    this.idx = user.getIdx();
    this.nickName = user.getNickname();
    this.profileImg = user.getProfileImg();
    this.birthDate = user.getBirthDate();
    this.isFriend = isFriend;
  }

}
