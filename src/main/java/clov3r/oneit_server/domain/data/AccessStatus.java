package clov3r.oneit_server.domain.data;

public enum AccessStatus {
  PRIVATE, PUBLIC;

  public Boolean isValid() {
    return this == PRIVATE || this == PUBLIC;
  }

  public static AccessStatus fromString(String accessStatus) {
    if (accessStatus.equals("PRIVATE")) {
      return PRIVATE;
    } else if (accessStatus.equals("PUBLIC")) {
      return PUBLIC;
    } else {
      return null;
    }
  }

}
