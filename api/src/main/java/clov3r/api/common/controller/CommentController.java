package clov3r.api.common.controller;

import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.common.config.security.Auth;
import clov3r.api.common.domain.DTO.CommentDTO;
import clov3r.api.common.domain.entity.Comment;
import clov3r.api.giftbox.domain.entity.GiftboxProduct;
import clov3r.api.common.domain.request.CommentRequest;
import clov3r.api.common.repository.CommentRepository;
import clov3r.api.giftbox.repository.GiftboxProductRepository;
import clov3r.api.giftbox.repository.GiftboxRepository;
import clov3r.api.common.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final GiftboxProductRepository giftboxProductRepository;
  private final GiftboxRepository giftboxRepository;
  private final CommentRepository commentRepository;

  @Tag(name = "선물바구니 상품 댓글 API", description = "선물바구니 상품 댓글 API 목록")
  @Operation(summary = "선물바구니 상품 댓글 추가", description = "선물 바구니 상품에 댓글 추가")
  @PostMapping("/api/v2/giftbox/{giftboxIdx}/products/{productIdx}/comments")
  public ResponseEntity<CommentDTO> createComment(
      @PathVariable Long giftboxIdx,
      @PathVariable Long productIdx,
      @RequestBody CommentRequest request,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // 선물바구니 댓글 생성 가능한 유저인지 확인
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.NOT_PARTICIPANT_OF_GIFTBOX);
    }
    GiftboxProduct giftboxProduct = giftboxProductRepository.findByGiftboxIdxAndProductIdx(giftboxIdx, productIdx);
    if (giftboxProduct == null) {
      throw new BaseExceptionV2(CustomErrorCode.GIFTBOX_PRODUCT_NOT_FOUND);
    }
    Comment comment = commentService.createComment(userIdx, giftboxProduct.getIdx(), request);
    CommentDTO commentDTO = new CommentDTO(comment);
    return ResponseEntity.ok(commentDTO);
  }

  @Tag(name = "선물바구니 상품 댓글 API", description = "선물바구니 상품 댓글 API 목록")
  @Operation(summary = "선물바구니 상품 댓글 삭제", description = "선물 바구니 상품에 댓글 삭제")
  @DeleteMapping("/api/v2/comments/{commentIdx}")
  public ResponseEntity<String> deleteComment(
      @PathVariable Long commentIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Comment comment = commentRepository.findByIdx(commentIdx);
    if (comment == null) {
      throw new BaseExceptionV2(CustomErrorCode.COMMENT_NOT_FOUND);
    }
    if (!comment.getWriter().getIdx().equals(userIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.CANNOT_DELETE_OTHERS_COMMENT);
    }
    commentService.deleteComment(commentIdx);
    return ResponseEntity.ok("댓글 삭제 성공");
  }

  @Tag(name = "선물바구니 상품 댓글 API", description = "선물바구니 상품 댓글 API 목록")
  @Operation(summary = "선물바구니 상품 댓글 리스트 조회", description = "선물 바구니 상품에 댓글 리스트 조회")
  @GetMapping("/api/v2/giftbox/{giftboxIdx}/products/{productIdx}/comments")
  public ResponseEntity<List<CommentDTO>> getComment(
      @PathVariable Long giftboxIdx,
      @PathVariable Long productIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // 선물바구니 조회 가능한 유저인지 확인
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(CustomErrorCode.NOT_PARTICIPANT_OF_GIFTBOX);
    }
    GiftboxProduct giftboxProduct = giftboxProductRepository.findByGiftboxIdxAndProductIdx(giftboxIdx, productIdx);
    if (giftboxProduct == null) {
      throw new BaseExceptionV2(CustomErrorCode.GIFTBOX_PRODUCT_NOT_FOUND);
    }
    List<CommentDTO> commentDTOList = commentService.getCommentList(giftboxProduct.getIdx());
    return ResponseEntity.ok(commentDTOList);
  }


}
