package clov3r.api.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class acceptInvitationRequest {
    private Long invitationIdx;
    private Long userIdx;

}
