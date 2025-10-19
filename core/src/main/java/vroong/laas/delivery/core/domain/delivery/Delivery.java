package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;
import lombok.ToString;
import vroong.laas.delivery.core.domain.shared.AggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 배송 Aggregate Root
 *
 * <p>배송의 핵심 비즈니스 로직과 상태를 관리합니다.
 *
 * <p>상태 전이:
 * - 정상: STARTED → ARRIVED → PICKED_UP → COMPLETED
 * - 취소: STARTED → CANCELLED, ARRIVED → CANCELLED
 */
@Getter
@ToString
public class Delivery {

    private Long id;
    private DeliveryNumber deliveryNumber;
    private Long orderId;
    private Long dispatchId;
    private Long agentId;
    private BigDecimal deliveryFee;
    private DeliveryStatus status;

    public Delivery(
        Long id,
        DeliveryNumber deliveryNumber,
        Long orderId,
        Long dispatchId,
        Long agentId,
        BigDecimal deliveryFee,
        DeliveryPolicy policy,
        DeliveryStatus status) {
        // 필수 값 검증
        if (id == null) {
            throw new IllegalArgumentException("ID는 필수입니다");
        }
        if (deliveryNumber == null) {
            throw new IllegalArgumentException("배송번호는 필수입니다");
        }
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
        if (status == null) {
            throw new IllegalArgumentException("배송 상태는 필수입니다");
        }
        if (policy == null) {
            throw new IllegalArgumentException("배송 정책은 필수입니다");
        }

        this.id = id;
        this.deliveryNumber = deliveryNumber;
        this.orderId = orderId;
        this.dispatchId = dispatchId;
        this.agentId = agentId;
        this.deliveryFee = deliveryFee;
        this.status = status;
    }

    public void arrive() {
        this.status = DeliveryStatus.PICKUP_ARRIVED;
    }

    public void pickup() {
        this.status = DeliveryStatus.PICKED_UP;
    }

    public void deliver() {
        this.status = DeliveryStatus.CANCELLED;
    }
}
