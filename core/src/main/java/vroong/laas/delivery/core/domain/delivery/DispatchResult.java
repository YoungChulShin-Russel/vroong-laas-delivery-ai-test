package vroong.laas.delivery.core.domain.delivery;

import java.math.BigDecimal;

/**
 * 배차 결과 정보
 *
 * <p>배차 서버의 Kafka 이벤트로부터 전달받은 정보를 담습니다.
 */
public record DispatchResult(
    Long dispatchId,
    Long orderId,
    Long agentId,
    BigDecimal deliveryFee
) {
    public DispatchResult {
        if (orderId == null) {
            throw new IllegalArgumentException("주문 ID는 필수입니다");
        }
        if (agentId == null) {
            throw new IllegalArgumentException("기사 ID는 필수입니다");
        }
        if (deliveryFee == null) {
            throw new IllegalArgumentException("배송비는 필수입니다");
        }
        if (deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다");
        }
    }
}

