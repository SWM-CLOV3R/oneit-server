package clov3r.api.auth.domain.dto;

import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.status.UserStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.Getter;

@Data
public class KakaoProfileDTO {
    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter
    @Data
    public class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }

    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        public Boolean name_needs_agreement;
        public String name;
        public Boolean has_phone_number;
        public Boolean phone_number_needs_agreement;
        public String phone_number;
        public Boolean has_birthyear;
        public Boolean birthyear_needs_agreement;
        public String birthyear;
        public Boolean has_birthday;
        public Boolean birthday_needs_agreement;
        public String birthday;
        public String birthday_type;

        @Data
        public class Profile {
            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url;
            public Boolean is_default_image;
            public Boolean is_default_nickname;
        }
    }

    public User toDomain() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String phoneNumber = "0"+kakao_account.phone_number.substring(4, 16);
        User user = User.builder()
            .email(kakao_account.email)
            .name(kakao_account.name)
            .nickname(properties.nickname)
            .profileImg(properties.profile_image)
            .phoneNumber(phoneNumber)
            .birthDate(LocalDate.parse(
                kakao_account.birthyear + kakao_account.birthday,
                formatter))
            .status(UserStatus.ACTIVE)
            .build();
        user.createBaseEntity();
        return user;
    }
}