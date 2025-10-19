package vroong.laas.delivery.core.domain.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vroong.laas.delivery.core.domain.delivery.command.CreateDeliveryCommand;
import vroong.laas.delivery.core.domain.delivery.dto.AgentInfo;
import vroong.laas.delivery.core.domain.delivery.required.DeliveryRepository;

/**
 * 배송 생성 Domain Service
 *
 * <p>배송 생성의 핵심 비즈니스 로직을 담당합니다.
 *
 * <p>주요 책임:
 * - 배송번호 생성
 * - 안심번호 생성 (통신사 호출)
 * - 배송 정책 조회 (주문 서버 호출)
 * - 배송 생성 및 저장
 * - 도메인 이벤트 발행
 */
@Service
@RequiredArgsConstructor
public class DeliveryCreator {

    private final DeliveryNumberGenerator deliveryNumberGenerator;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery create(DispatchResult dispatchResult) {
        DeliveryNumber deliveryNumber = deliveryNumberGenerator.generate();

        NewDelivery newDelivery = new NewDelivery(
            deliveryNumber,
            dispatchResult.dispatchId(),
            dispatchResult.orderId(),
            dispatchResult.orderId(),
            dispatchResult.deliveryFee());

        Delivery delivery = deliveryRepository.save(newDelivery);

        // 배송 시작 이벤트

        return delivery;
    }


}
