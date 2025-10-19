package vroong.laas.delivery.core.domain.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;

public record RoutingStatusSequence(
    int sequence,
    DeliveryStatus deliveryStatus,
    boolean required
) {

}
