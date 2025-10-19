package vroong.laas.delivery.core.domain.delivery;

import java.math.BigDecimal;

public record NewDelivery(
    DeliveryNumber deliveryNumber,
    Long dispatchId,
    Long orderId,
    Long agentId,
    BigDecimal deliveryFee,
    DeliveryStatus status
) {
    public NewDelivery(
        DeliveryNumber deliveryNumber,
        Long dispatchId,
        Long orderId,
        Long agentId,
        BigDecimal deliveryFee) {
        this(deliveryNumber, dispatchId, orderId, agentId, deliveryFee, DeliveryStatus.STARTED);
    }
}
