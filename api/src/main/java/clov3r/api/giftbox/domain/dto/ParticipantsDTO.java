package clov3r.api.giftbox.domain.dto;

import clov3r.api.auth.domain.entity.User;
import clov3r.api.giftbox.domain.data.GiftboxUserRole;
import clov3r.api.giftbox.domain.entity.GiftboxUser;
import java.util.Optional;
import lombok.Data;

@Data
public class ParticipantsDTO {
  private Long userIdx;
  private String nickname;
  private String name;
  private String profileImage;
  private GiftboxUserRole userRole;


  public ParticipantsDTO(GiftboxUser roomUser) {
    User user = roomUser.getUser();
    this.userIdx = user.getIdx();
    this.nickname = user.getNickname();
    this.name = user.getName();
    this.profileImage = user.getProfileImg();
    this.userRole = roomUser.getUserRole();
  }
}
