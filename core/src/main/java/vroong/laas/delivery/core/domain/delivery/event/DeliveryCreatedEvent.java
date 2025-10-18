package vroong.laas.delivery.core.domain.delivery.event;

import lombok.Getter;
import vroong.laas.delivery.core.domain.shared.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 배송 생성 도메인 이벤트
 */
@Getter
public class DeliveryCreatedEvent implements DomainEvent {

    private final Long deliveryId;
    private final String deliveryNumber;
    private final Long orderId;
    private final Long dispatchId;
    private final Long agentId;
    private final BigDecimal deliveryFee;
    private final String status;
    private final String safePhoneNumber;
    private final boolean alcoholDelivery;
    private final boolean contactlessDelivery;
    private final Instant pickupRequestTime;
    private final Instant occurredAt;

    public DeliveryCreatedEvent(
        Long deliveryId,
        String deliveryNumber,
        Long orderId,
        Long dispatchId,
        Long agentId,
        BigDecimal deliveryFee,
        String status,
        String safePhoneNumber,
        boolean alcoholDelivery,
        boolean contactlessDelivery,
        Instant pickupRequestTime,
        Instant occurredAt
    ) {
        this.deliveryId = deliveryId;
        this.deliveryNumber = deliveryNumber;
        this.orderId = orderId;
        this.dispatchId = dispatchId;
        this.agentId = agentId;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.safePhoneNumber = safePhoneNumber;
        this.alcoholDelivery = alcoholDelivery;
        this.contactlessDelivery = contactlessDelivery;
        this.pickupRequestTime = pickupRequestTime;
        this.occurredAt = occurredAt;
    }

    /**
     * Delivery에서 이벤트 생성
     */
    public static DeliveryCreatedEvent from(vroong.laas.delivery.core.domain.delivery.Delivery delivery) {
        return new DeliveryCreatedEvent(
            delivery.getId(),
            delivery.getDeliveryNumber().getValue(),
            delivery.getOrderId(),
            delivery.getDispatchId(),
            delivery.getAgentId(),
            delivery.getDeliveryFee(),
            delivery.getStatus().name(),
            delivery.getSafePhoneNumber().getSafePhoneNumber(),
            delivery.getPolicy().isAlcoholDelivery(),
            delivery.getPolicy().isContactlessDelivery(),
            delivery.getPolicy().getPickupRequestTime(),
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
