package clov3r.api.domain.request;

import clov3r.api.domain.data.status.AccessStatus;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PostGiftboxRequest {
    private String name;
    private LocalDate deadline;
}
