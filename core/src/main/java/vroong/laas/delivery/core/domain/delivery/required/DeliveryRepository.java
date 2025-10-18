package vroong.laas.delivery.core.domain.delivery.required;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryNumber;

import java.util.Optional;

/**
 * 배송 Repository Port
 *
 * <p>배송의 영속성을 관리하는 인터페이스입니다.
 *
 * <p>Infrastructure Layer에서 구현됩니다.
 */
public interface DeliveryRepository {

    /**
     * 배송 생성 및 저장
     *
     * <p>Delivery Entity를 생성하고 저장한 후, 완전한 Delivery 모델(id 포함)을 반환합니다.
     *
     * @param deliveryNumber 배송번호
     * @param orderId 주문 ID
     * @param dispatchId 배차 ID
     * @param agentId 기사 ID
     * @param deliveryFee 배송비
     * @param safePhoneNumber 안심번호
     * @param policy 배송 정책
     * @return 저장된 Delivery (id 할당됨)
     */
    Delivery store(
        DeliveryNumber deliveryNumber,
        Long orderId,
        Long dispatchId,
        Long agentId,
        java.math.BigDecimal deliveryFee,
        vroong.laas.delivery.core.domain.delivery.DeliverySafePhoneNumber safePhoneNumber,
        vroong.laas.delivery.core.domain.delivery.DeliveryPolicy policy
    );

    /**
     * ID로 배송 조회
     *
     * @param id 배송 ID
     * @return 배송 (없으면 empty)
     */
    Optional<Delivery> findById(Long id);

    /**
     * 배송번호로 배송 조회
     *
     * @param deliveryNumber 배송번호
     * @return 배송 (없으면 empty)
     */
    Optional<Delivery> findByDeliveryNumber(DeliveryNumber deliveryNumber);

    /**
     * 주문 ID로 배송 조회
     *
     * @param orderId 주문 ID
     * @return 배송 (없으면 empty)
     */
    Optional<Delivery> findByOrderId(Long orderId);

    /**
     * 배송번호 존재 여부 확인
     *
     * @param deliveryNumber 배송번호
     * @return 존재 여부
     */
    boolean existsByDeliveryNumber(DeliveryNumber deliveryNumber);

    /**
     * 배송 저장 (상태 변경 등)
     *
     * @param delivery 배송
     * @return 저장된 배송
     */
    Delivery save(Delivery delivery);
}
