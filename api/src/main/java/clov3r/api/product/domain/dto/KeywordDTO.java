package clov3r.api.product.domain.dto;

import clov3r.domain.domains.entity.Keyword;
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
