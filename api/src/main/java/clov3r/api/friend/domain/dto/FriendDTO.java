package clov3r.api.friend.domain.dto;

import clov3r.domain.domains.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {

      private Long idx;
      private String name;
      private String nickName;
      private String profileImg;
      private LocalDate birthDate;

      public FriendDTO(User user) {
        this.idx = user.getIdx();
        this.name = user.getName();
        this.nickName = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.birthDate = user.getBirthDate();
      }

}
