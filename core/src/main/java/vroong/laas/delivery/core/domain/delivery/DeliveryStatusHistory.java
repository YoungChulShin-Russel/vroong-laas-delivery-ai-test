package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;

import java.time.Instant;

/**
 * 배송 상태 이력 Value Object
 *
 * <p>배송 상태 변경 이력을 추적합니다.
 */
@Getter
public class DeliveryStatusHistory {

    private final Long id;
    private final Long deliveryId;
    private final DeliveryStatus status;
    private final boolean isCancelled;
    private final String reason;
    private final Instant changedAt;

    public DeliveryStatusHistory(
        Long id,
        Long deliveryId,
        DeliveryStatus status,
        Boolean isCancelled,
        String reason,
        Instant changedAt
    ) {
        if (deliveryId == null) {
            throw new IllegalArgumentException("배송 ID는 필수입니다");
        }
        if (status == null) {
            throw new IllegalArgumentException("배송 상태는 필수입니다");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("변경 사유는 필수입니다");
        }
        if (changedAt == null) {
            throw new IllegalArgumentException("변경 시간은 필수입니다");
        }

        this.id = id;
        this.deliveryId = deliveryId;
        this.status = status;
        this.isCancelled = (isCancelled != null) ? isCancelled : false;
        this.reason = reason;
        this.changedAt = changedAt;
    }
}
