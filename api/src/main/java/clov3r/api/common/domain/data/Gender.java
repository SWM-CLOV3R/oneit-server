package clov3r.api.common.domain.data;

public enum Gender {
    FEMALE,
    MALE,
    UNISEX;

    public static boolean isValid(Gender gender) {
        // Check if gender is one of the specified enum values
        return gender.equals(Gender.FEMALE) || gender.equals(Gender.MALE) || gender.equals(Gender.UNISEX);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
