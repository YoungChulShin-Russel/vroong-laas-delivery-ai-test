package vroong.laas.delivery.core.domain.delivery.required;

import vroong.laas.delivery.core.domain.delivery.dto.OrderDeliveryPolicy;

/**
 * 주문 서비스 인터페이스
 */
public interface OrderService {
    OrderDeliveryPolicy getDeliveryPolicy(Long orderId);
}

