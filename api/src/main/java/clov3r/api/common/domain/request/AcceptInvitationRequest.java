package clov3r.api.common.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AcceptInvitationRequest {
    private Long invitationIdx;
    private Long userIdx;

}
