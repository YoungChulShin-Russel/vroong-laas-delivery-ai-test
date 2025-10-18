package vroong.laas.delivery.core.domain.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vroong.laas.delivery.core.domain.delivery.exception.DeliveryNotFoundException;
import vroong.laas.delivery.core.domain.delivery.required.DeliveryRepository;

/**
 * 배송 조회 Domain Service
 *
 * <p>배송 조회의 핵심 비즈니스 로직을 담당합니다.
 *
 * <p>주요 책임:
 * - 배송 조회 (ID, 배송번호, 주문 ID)
 * - 배송 존재 여부 확인
 * - 조회 권한 검증 (필요시)
 */
@Service
@RequiredArgsConstructor
public class DeliveryReader {

    private final DeliveryRepository deliveryRepository;

    /**
     * ID로 배송 조회
     *
     * @param id 배송 ID
     * @return 배송
     * @throws DeliveryNotFoundException 배송을 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository
            .findById(id)
            .orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    /**
     * 배송번호로 배송 조회
     *
     * @param deliveryNumber 배송번호
     * @return 배송
     * @throws DeliveryNotFoundException 배송을 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public Delivery getDeliveryByDeliveryNumber(DeliveryNumber deliveryNumber) {
        return deliveryRepository
            .findByDeliveryNumber(deliveryNumber)
            .orElseThrow(() -> new DeliveryNotFoundException(deliveryNumber.getValue()));
    }

    /**
     * 주문 ID로 배송 조회
     *
     * @param orderId 주문 ID
     * @return 배송
     * @throws DeliveryNotFoundException 배송을 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryRepository
            .findByOrderId(orderId)
            .orElseThrow(() -> new DeliveryNotFoundException("주문 ID: " + orderId));
    }

    /**
     * 배송 존재 여부 확인
     *
     * @param deliveryNumber 배송번호
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean existsByDeliveryNumber(DeliveryNumber deliveryNumber) {
        return deliveryRepository.existsByDeliveryNumber(deliveryNumber);
    }
}
