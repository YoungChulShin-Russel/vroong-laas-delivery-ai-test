package vroong.laas.delivery.core.domain.delivery.event;

import lombok.Getter;
import vroong.laas.delivery.core.domain.shared.event.DomainEvent;

import java.time.Instant;

/**
 * 배송 상태 변경 도메인 이벤트
 */
@Getter
public class DeliveryStatusChangedEvent implements DomainEvent {

    private final Long deliveryId;
    private final String deliveryNumber;
    private final String previousStatus;
    private final String newStatus;
    private final String reason;
    private final Instant occurredAt;

    public DeliveryStatusChangedEvent(
        Long deliveryId,
        String deliveryNumber,
        String previousStatus,
        String newStatus,
        String reason,
        Instant occurredAt
    ) {
        this.deliveryId = deliveryId;
        this.deliveryNumber = deliveryNumber;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    /**
     * Delivery에서 이벤트 생성
     */
    public static DeliveryStatusChangedEvent from(
        vroong.laas.delivery.core.domain.delivery.Delivery delivery,
        vroong.laas.delivery.core.domain.delivery.DeliveryStatus newStatus,
        String reason
    ) {
        return new DeliveryStatusChangedEvent(
            delivery.getId(),
            delivery.getDeliveryNumber().getValue(),
            delivery.getStatus().name(),
            newStatus.name(),
            reason,
            Instant.now()
        );
    }

    @Override
    public String aggregateType() {
        return "Delivery";
    }

    @Override
    public String aggregateId() {
        return String.valueOf(deliveryId);
    }
}
