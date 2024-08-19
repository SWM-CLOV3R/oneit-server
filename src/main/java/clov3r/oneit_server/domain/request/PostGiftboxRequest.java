package clov3r.oneit_server.domain.request;

import clov3r.oneit_server.domain.data.status.AccessStatus;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
public class PostGiftboxRequest {
    private String name;
    private String description;
    private LocalDate deadline;
    private AccessStatus accessStatus;
}
