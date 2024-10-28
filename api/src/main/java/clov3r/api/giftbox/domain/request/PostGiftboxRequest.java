package clov3r.api.giftbox.domain.request;

import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PostGiftboxRequest {
    private String name;
    private LocalDate deadline;
}
