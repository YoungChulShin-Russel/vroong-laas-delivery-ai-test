package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;

import java.time.Instant;

/**
 * 배송 정책 Value Object
 *
 * <p>주문 서버에서 가져온 배송 정책 정보를 담습니다.
 */
@Getter
public class DeliveryPolicy {

    private final boolean alcoholDelivery;
    private final boolean contactlessDelivery;
    private final Instant pickupRequestTime;

    public DeliveryPolicy(
        boolean alcoholDelivery,
        boolean contactlessDelivery,
        Instant pickupRequestTime
    ) {
        if (pickupRequestTime == null) {
            throw new IllegalArgumentException("픽업 요청 시간은 필수입니다");
        }

        this.alcoholDelivery = alcoholDelivery;
        this.contactlessDelivery = contactlessDelivery;
        this.pickupRequestTime = pickupRequestTime;
    }

    /**
     * 배송 정책 생성
     */
    public static DeliveryPolicy create(
        boolean alcoholDelivery,
        boolean contactlessDelivery,
        Instant pickupRequestTime
    ) {
        return new DeliveryPolicy(alcoholDelivery, contactlessDelivery, pickupRequestTime);
    }

}
