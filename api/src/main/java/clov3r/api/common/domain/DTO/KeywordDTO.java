package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.entity.Keyword;
import lombok.Data;

@Data
public class KeywordDTO {
    private Long idx;
    private String name;
    private String description;

    public KeywordDTO(Keyword keyword) {
        this.idx = keyword.getIdx();
        this.name = keyword.getName();
        this.description = keyword.getDescription();
    }
}
