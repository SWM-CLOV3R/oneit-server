package clov3r.api.controller;

import static clov3r.api.error.errorcode.CommonErrorCode.REQUEST_ERROR;
import static clov3r.api.error.errorcode.CustomErrorCode.ALREADY_PARTICIPANT_OF_GIFTBOX;
import static clov3r.api.error.errorcode.CustomErrorCode.ALREADY_USED_INVITATION;
import static clov3r.api.error.errorcode.CustomErrorCode.DATE_BEFORE_NOW;
import static clov3r.api.error.errorcode.CustomErrorCode.GIFTBOX_NOT_FOUND;
import static clov3r.api.error.errorcode.CustomErrorCode.INVALID_ACCESS_STATUS;
import static clov3r.api.error.errorcode.CustomErrorCode.INVITATION_NOT_FOUND;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_MANAGER_OF_GIFTBOX;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_PARTICIPANT_OF_GIFTBOX;
import static clov3r.api.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import clov3r.api.config.security.Auth;
import clov3r.api.domain.DTO.GiftboxDTO;
import clov3r.api.domain.DTO.InvitationUserDTO;
import clov3r.api.domain.DTO.ParticipantsDTO;
import clov3r.api.domain.data.status.AccessStatus;
import clov3r.api.domain.data.status.InvitationStatus;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.GiftboxUser;
import clov3r.api.domain.entity.Notification;
import clov3r.api.domain.request.PostGiftboxRequest;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.GiftboxRepository;
import clov3r.api.repository.NotificationRepository;
import clov3r.api.repository.ProductRepository;
import clov3r.api.repository.UserRepository;
import clov3r.api.service.GiftboxService;
import clov3r.api.service.NotificationService;
import clov3r.api.service.common.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GiftboxControllerV2 {

  private final GiftboxService giftboxService;
  private final GiftboxRepository giftboxRepository;
  private final S3Service s3Service;
  private final UserRepository userRepository;


  @Tag(name = "선물바구니 API", description = "선물바구니 CRUD API 목록")
  @Operation(summary = "선물바구니 생성", description = "선물 바구니 생성, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PostMapping(value = "/api/v2/giftbox", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Long> createGiftbox(
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (request.getName() == null || request.getDeadline() == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (request.getDeadline().isBefore(LocalDateTime.now().toLocalDate())) {
      throw new BaseExceptionV2(DATE_BEFORE_NOW);
    }
    // create Gift box
    Long giftboxIdx = giftboxService.createGiftbox(request, userIdx);

    // upload image if exists
    if (image != null) {
      String imageUrl = s3Service.upload(image, "giftbox-profile");
      giftboxService.updateGiftboxImageUrl(giftboxIdx, imageUrl);
    }
    return ResponseEntity.ok(giftboxIdx);
  }

  @Tag(name = "선물바구니 API", description = "선물바구니 CRUD API 목록")
  @Operation(summary = "선물바구니 상세 조회", description = "선물 바구니의 idx로 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v2/giftbox/{giftboxIdx}")
  public ResponseEntity<GiftboxDTO> getGiftboxByIdx(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(giftboxIdx)) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }

    // get giftbox
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 조회 가능함
    }

    // get participants of giftbox
    List<GiftboxUser> participants = giftboxRepository.findParticipantsOfGiftbox(giftboxIdx);
    List<ParticipantsDTO> participantsDTOList = participants.stream()
        .map(participant -> new ParticipantsDTO(
            participant.getUser().getIdx(),
            participant.getUser().getNickname(),
            participant.getUser().getName(),
            participant.getUser().getProfileImg(),
            participant.getUserRole()
        ))
        .toList();

    // make giftbox detail dto
    GiftboxDTO giftboxDTO = new GiftboxDTO(giftbox, participantsDTOList);
    return ResponseEntity.ok(giftboxDTO);
  }

  @Tag(name = "선물바구니 API", description = "선물바구니 CRUD API 목록")
  @Operation(summary = "해당 유저의 선물바구니 목록 조회", description = "해당 유저가 소유한 모든 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v2/giftbox")
  public ResponseEntity<List<GiftboxDTO>> getGiftboxList(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }

    // get giftbox list of the user
    List<Giftbox> giftboxList = giftboxRepository.findGiftboxOfUser(userIdx);
    List<GiftboxDTO> giftboxDTOList = giftboxList.stream()
        .map(giftbox -> {
          List<GiftboxUser> participants = giftboxRepository.findParticipantsOfGiftbox(
              giftbox.getIdx());
          List<ParticipantsDTO> participantsDTOList = participants.stream()
              .map(participant -> new ParticipantsDTO(
                  participant.getUser().getIdx(),
                  participant.getUser().getNickname(),
                  participant.getUser().getName(),
                  participant.getUser().getProfileImg(),
                  participant.getUserRole()
              ))
              .toList();
          return new GiftboxDTO(giftbox, participantsDTOList);
        })
        .toList();
    return ResponseEntity.ok(giftboxDTOList);
  }

  @Tag(name = "선물바구니 API", description = "선물바구니 CRUD API 목록")
  @Operation(summary = "선물바구니 삭제", description = "선물 바구니의 idx로 선물 바구니 삭제")
  @DeleteMapping("/api/v2/giftbox/{giftboxIdx}")
  public ResponseEntity<String> deleteGiftboxByIdx(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isManagerOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 삭제 가능함
    }

    // delete giftbox
    giftboxRepository.deleteById(giftboxIdx);
    return ResponseEntity.ok(giftboxIdx + "번 선물 바구니가 삭제되었습니다.");
  }

  @Tag(name = "선물바구니 API", description = "선물바구니 CRUD API 목록")
  @Operation(summary = "선물바구니 수정", description = "선물 바구니의 idx로 선물 바구니 수정, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PutMapping(value = "/api/v2/giftbox/{giftboxIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<GiftboxDTO> updateGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (request.getName() == null || request.getDeadline() == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isManagerOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 수정 가능함
    }
    if (request.getDeadline().isBefore(LocalDateTime.now().toLocalDate())) {
      throw new BaseExceptionV2(DATE_BEFORE_NOW);
    }

    // update giftbox
    giftboxService.updateGiftbox(giftboxIdx, request);
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    String imageUrl = giftbox.getImageUrl();
    // upload image if exists
    if (image != null) {
      imageUrl = s3Service.upload(image, "giftbox-profile");
      giftboxService.updateGiftboxImageUrl(giftboxIdx, imageUrl);
    }

    // get participants of giftbox
    List<GiftboxUser> participants = giftbox.getParticipants();
    List<ParticipantsDTO> participantsDTOList = participants.stream()
        .map(participant -> new ParticipantsDTO(
            participant.getUser().getIdx(),
            participant.getUser().getNickname(),
            participant.getUser().getName(),
            participant.getUser().getProfileImg(),
            participant.getUserRole()
        ))
        .toList();

    // make giftbox detail dto
    GiftboxDTO giftboxDTO = new GiftboxDTO(
        giftboxIdx,
        request.getName(),
        request.getDeadline(),
        imageUrl,
        giftbox.getCreatedUserIdx(),
        participantsDTOList,
        giftbox.getCreatedAt());
    return ResponseEntity.ok(giftboxDTO);
  }


  // 선물 바구니 초대 API (PENDING)
  @Tag(name = "선물바구니 초대 API", description = "선물바구니 초대 API 목록")
  @Operation(summary = "선물바구니 초대장 생성", description = "해당 선물바구니에 대한 초대가 생성되고, 초대 상태는 PENDING 됩니다.")
  @PostMapping("/api/v2/giftbox/{giftboxIdx}/invitation")
  public ResponseEntity<InvitationUserDTO> inviteUserToGiftBox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(giftboxIdx)) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(
          NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 초대 가능함
    }

    // invite user to giftbox
    Long invitationIdx = giftboxService.inviteUserToGiftBox(giftboxIdx, userIdx);
    return ResponseEntity.ok(new InvitationUserDTO(invitationIdx));
  }

  // 선물 바구니 초대 수락 API (ACCEPTED)
  @Tag(name = "선물바구니 초대 API", description = "선물바구니 초대 API 목록")
  @Operation(summary = "선물바구니 초대 수락", description = "해당 선물바구니에 대한 초대가 수락되고 수락한 유저 정보가 입력됩니다. 초대 상태가 ACCEPTED로 변경됩니다.")
  @PatchMapping("/api/v2/giftbox/invitation/{invitationIdx}/status")
  public ResponseEntity<String> acceptInvitationToGiftBox(
      @PathVariable("invitationIdx") Long invitationIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (invitationIdx == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsInvitationOfGiftbox(invitationIdx)) {
      throw new BaseExceptionV2(INVITATION_NOT_FOUND);
    }
    GiftboxUser giftboxUser = giftboxRepository.findGiftboxByInvitationIdx(invitationIdx);
    if (giftboxUser.getInvitationStatus().equals(InvitationStatus.ACCEPTED)
        && giftboxUser.getUser().getIdx() != null) {
      throw new BaseExceptionV2(ALREADY_USED_INVITATION);
    }
    if (giftboxRepository.existParticipantOfGiftbox(userIdx, giftboxUser.getGiftbox().getIdx())) {
      throw new BaseExceptionV2(ALREADY_PARTICIPANT_OF_GIFTBOX);
    }
    if (!giftboxRepository.existsById(giftboxUser.getGiftbox().getIdx())) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }

    // accept invitation to giftbox
    giftboxService.acceptInvitationToGiftBox(giftboxUser, userIdx, invitationIdx);
    return ResponseEntity.ok(
        "유저 " + userIdx + "님이 " + giftboxUser.getGiftbox().getIdx() + "번 선물 바구니에 참여하였습니다.");
  }

  @Tag(name = "선물바구니 API", description = "선물바구니 API 목록")
  @Operation(summary = "선물바구니 참여자 목록 조회", description = "해당 선물바구니의 참여자 목록을 조회합니다.")
  @GetMapping("/api/v2/giftbox/{giftboxIdx}/participants")
  public ResponseEntity<List<ParticipantsDTO>> getParticipantsOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(giftboxIdx)) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 조회 가능함
    }

    List<GiftboxUser> participants = giftboxRepository.findParticipantsOfGiftbox(giftboxIdx);
    List<ParticipantsDTO> participantsDTOList = participants.stream()
        .map(participant -> new ParticipantsDTO(
            participant.getUser().getIdx(),
            participant.getUser().getNickname(),
            participant.getUser().getName(),
            participant.getUser().getProfileImg(),
            participant.getUserRole()
        ))
        .toList();
    return ResponseEntity.ok(participantsDTOList);
  }

}
