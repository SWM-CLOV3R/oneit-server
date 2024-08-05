package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Keyword;
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
