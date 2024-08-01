package clov3r.oneit_server.domain.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class PostGiftboxRequest {
    private String name;
    private String description;
    private Date deadline;
    private Long createdUserIdx;
    private String accessStatus;
}
