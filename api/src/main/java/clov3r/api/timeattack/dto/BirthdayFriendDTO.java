package clov3r.api.timeattack.dto;

import clov3r.domain.domains.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BirthdayFriendDTO {

  private Long idx;
  private String name;
  private String nickName;
  private String profileImg;
  private LocalDate birthDate;
  @Setter
  private boolean timeAttackAlarm;
  @Setter
  private boolean haveWishList;

  public BirthdayFriendDTO(User user) {
    this.idx = user.getIdx();
    this.name = user.getName();
    this.nickName = user.getNickname();
    this.profileImg = user.getProfileImg();
    this.birthDate = user.getBirthDate();
  }

}
