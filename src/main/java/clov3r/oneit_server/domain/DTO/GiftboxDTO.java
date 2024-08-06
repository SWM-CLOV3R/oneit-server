package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Giftbox;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiftboxDTO {
    private Long idx;
    private String name;
    private String description;
    private Date deadline;
    private String imageUrl;
    private Long createdUserIdx;
    private String accessStatus;

    public GiftboxDTO(Long idx, String name, String description, Date deadline, String imageUrl, Long createdUserIdx, String accessStatus) {
        this.idx = idx;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        this.createdUserIdx = createdUserIdx;
        this.accessStatus = accessStatus;
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
