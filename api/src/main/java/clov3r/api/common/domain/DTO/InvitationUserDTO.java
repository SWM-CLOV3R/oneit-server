package clov3r.api.common.domain.DTO;

import lombok.Data;

@Data
public class InvitationUserDTO {
  private Long invitationIdx;

  public InvitationUserDTO(Long invitationIdx) {
    this.invitationIdx = invitationIdx;
  }
}
