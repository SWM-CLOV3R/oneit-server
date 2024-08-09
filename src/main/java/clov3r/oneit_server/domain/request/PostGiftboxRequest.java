package clov3r.oneit_server.domain.request;

import clov3r.oneit_server.domain.data.AccessStatus;
import java.time.LocalDate;
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
    private LocalDate deadline;
    private String accessStatus;
}
