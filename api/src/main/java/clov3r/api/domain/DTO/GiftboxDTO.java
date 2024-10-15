package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.AccessStatus;
import clov3r.api.domain.entity.Giftbox;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiftboxDTO {
    private Long idx;
    private String name;
    private String description;
    private LocalDate deadline;
    private String imageUrl;
    private Long createdUserIdx;
    private AccessStatus accessStatus;
    private List<ParticipantsDTO> participants;

    public GiftboxDTO(Long idx, String name, String description, LocalDate deadline, String imageUrl, Long createdUserIdx, AccessStatus accessStatus, List<ParticipantsDTO> participants) {
        this.idx = idx;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        this.createdUserIdx = createdUserIdx;
        this.accessStatus = accessStatus;
        this.participants = participants;
    }

    public GiftboxDTO(Giftbox giftbox) {
        this.idx = giftbox.getIdx();
        this.name = giftbox.getName();
        this.description = giftbox.getDescription();
        this.deadline = giftbox.getDeadline();
        this.imageUrl = giftbox.getImageUrl();
        this.createdUserIdx = giftbox.getCreatedUserIdx();
        this.accessStatus = giftbox.getAccessStatus();
    }

}
