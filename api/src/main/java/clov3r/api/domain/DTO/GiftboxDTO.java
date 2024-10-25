package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.AccessStatus;
import clov3r.api.domain.entity.Giftbox;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    public GiftboxDTO(Long idx, String name, LocalDate deadline, String imageUrl, Long createdUserIdx, List<ParticipantsDTO> participants, LocalDateTime createdAt) {
        this.idx = idx;
        this.name = name;
//        this.description = description;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        this.createdUserIdx = createdUserIdx;
//        this.accessStatus = accessStatus;
        this.participants = participants;
        this.createdAt = createdAt;
    }

    public GiftboxDTO(Giftbox giftbox, List<ParticipantsDTO> participants) {
        this.idx = giftbox.getIdx();
        this.name = giftbox.getName();
        this.deadline = giftbox.getDeadline();
        this.imageUrl = giftbox.getImageUrl();
        this.createdUserIdx = giftbox.getCreatedUserIdx();
        this.participants = participants;
        this.createdAt = giftbox.getCreatedAt();
    }

}
