package clov3r.api.common.domain.DTO;

import java.util.List;
import lombok.Data;

@Data
public class KakaoFriendDTO {

  @Data
  private static class Elements {
    private String profile_nickname;
    private String profile_thumbnail_image;
    private boolean allowed_msg;
    private Long id;
    private String uuid;
    private boolean favorite;
  }

  private List<Elements> elements;
  private int total_count;
  private String after_url;
  private int favorite_count;
}
