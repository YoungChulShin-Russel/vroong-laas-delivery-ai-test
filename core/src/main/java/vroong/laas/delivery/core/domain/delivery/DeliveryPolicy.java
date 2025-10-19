package vroong.laas.delivery.core.domain.delivery;

import java.time.Instant;

/**
 * 주문 배송 정책 DTO
 */
public record DeliveryPolicy(
    boolean alcoholDelivery,
    boolean contactlessDelivery,
    Instant pickupRequestTime
) {}

