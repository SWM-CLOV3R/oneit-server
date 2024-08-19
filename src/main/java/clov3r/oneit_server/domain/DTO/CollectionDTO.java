package clov3r.oneit_server.domain.DTO;

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
