package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.data.GiftboxUserRole;
import lombok.Data;

@Data
public class ParticipantsDTO {
  private Long userIdx;
  private String nickname;
  private String name;
  private String profileImage;
  private GiftboxUserRole userRole;

  public ParticipantsDTO(Long userIdx, String nickname, String name, String profileImage, GiftboxUserRole userRole) {
    this.userIdx = userIdx;
    this.nickname = nickname;
    this.name = name;
    this.profileImage = profileImage;
    this.userRole = userRole;
  }
}
