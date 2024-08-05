package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.response.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftboxService {

    private final GiftboxRepository giftboxRepository;

    @Transactional
    public Long createGiftbox(PostGiftboxRequest request) {

        Giftbox newGiftbox = new Giftbox(
                request.getName(),
                request.getDescription(),
                request.getDeadline(),
                request.getCreatedUserIdx(),
                request.getAccessStatus()
        );
        Giftbox saveGiftbox = giftboxRepository.save(newGiftbox);

        // 생성한 유저 idx와 선물 바구니 idx를 연결
        try {
            giftboxRepository.createGiftboxManager(request.getCreatedUserIdx(), saveGiftbox.getIdx());
        } catch (BaseException e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

        // 방금 저장한 giftbox의 idx를 가져옴
        return saveGiftbox.getIdx();
    }

    @Transactional
    public void updateGiftboxImageUrl(Long idx, String imageUrl) {
        giftboxRepository.updateImageUrl(idx, imageUrl);
    }

    public void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request) {
        giftboxRepository.updateGiftbox(giftboxIdx, request);
    }
}
