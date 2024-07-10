package clov3r.oneit_server.domain.data;

public enum Gender {
    FEMALE,
    MALE,
    UNISEX;

    public static boolean isValid(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
