package vroong.laas.delivery.core.domain.delivery.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 배송 생성 Command
 *
 * <p>배차 완료 이벤트에서 배송을 생성할 때 사용됩니다.
 */
@Builder
public record CreateDeliveryCommand(
    Long orderId,
    Long dispatchId,
    Long agentId,
    BigDecimal deliveryFee,
    String agentPhoneNumber,
    boolean alcoholDelivery,
    boolean contactlessDelivery,
    Instant pickupRequestTime,
    Instant dispatchedAt
) {
    /**
     * 필수 값 검증
     */
    public CreateDeliveryCommand {
        if (orderId == null) {
            throw new IllegalArgumentException("주문 ID는 필수입니다");
        }
        if (dispatchId == null) {
            throw new IllegalArgumentException("배차 ID는 필수입니다");
        }
        if (agentId == null) {
            throw new IllegalArgumentException("기사 ID는 필수입니다");
        }
        if (deliveryFee == null || deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다");
        }
        if (agentPhoneNumber == null || agentPhoneNumber.isBlank()) {
            throw new IllegalArgumentException("기사 전화번호는 필수입니다");
        }
        if (pickupRequestTime == null) {
            throw new IllegalArgumentException("픽업 요청 시간은 필수입니다");
        }
        if (dispatchedAt == null) {
            throw new IllegalArgumentException("배차 시간은 필수입니다");
        }
    }
}
