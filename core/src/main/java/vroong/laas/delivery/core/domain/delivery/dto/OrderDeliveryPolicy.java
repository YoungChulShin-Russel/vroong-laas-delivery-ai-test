package vroong.laas.delivery.core.domain.delivery.dto;

import java.time.Instant;

/**
 * 주문 배송 정책 DTO
 */
public record OrderDeliveryPolicy(
    boolean alcoholDelivery,
    boolean contactlessDelivery,
    Instant pickupRequestTime
) {}

