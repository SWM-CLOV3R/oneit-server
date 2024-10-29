package clov3r.api.friend.domain.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class FriendDTO {

      private Long idx;
      private String name;
      private String nickName;
      private String profileImg;
      private LocalDate birthDate;

      public FriendDTO(Long idx, String name, String nickName, String profileImg, LocalDate birthDate) {
        this.idx = idx;
        this.name = name;
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.birthDate = birthDate;
      }

}
