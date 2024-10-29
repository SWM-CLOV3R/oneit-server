package clov3r.api.common.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDTO {

  private Long idx;
  private String name;
  private String description;
  private String thumbnailUrl;

}
