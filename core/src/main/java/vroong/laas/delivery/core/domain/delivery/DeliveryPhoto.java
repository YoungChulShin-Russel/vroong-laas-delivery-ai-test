package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;

import java.time.Instant;

/**
 * 배송 사진 Value Object
 *
 * <p>비대면 배송 시 필수 사진을 관리합니다.
 */
@Getter
public class DeliveryPhoto {

    private final Long id;
    private final Long deliveryId;
    private final String photoUrl;
    private final Instant uploadedAt;

    public DeliveryPhoto(
        Long id,
        Long deliveryId,
        String photoUrl,
        Instant uploadedAt
    ) {
        if (deliveryId == null) {
            throw new IllegalArgumentException("배송 ID는 필수입니다");
        }
        if (photoUrl == null || photoUrl.isBlank()) {
            throw new IllegalArgumentException("사진 URL은 필수입니다");
        }
        if (uploadedAt == null) {
            throw new IllegalArgumentException("업로드 시간은 필수입니다");
        }

        this.id = id;
        this.deliveryId = deliveryId;
        this.photoUrl = photoUrl;
        this.uploadedAt = uploadedAt;
    }
}
