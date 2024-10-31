package clov3r.api.giftbox.domain.dto;

import lombok.Data;

@Data
public class InvitationUserDTO {
  private Long invitationIdx;

  public InvitationUserDTO(Long invitationIdx) {
    this.invitationIdx = invitationIdx;
  }
}
