package vroong.laas.delivery.core.domain.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vroong.laas.delivery.core.domain.delivery.command.CreateDeliveryCommand;
import vroong.laas.delivery.core.domain.delivery.dto.AgentInfo;
import vroong.laas.delivery.core.domain.delivery.dto.OrderDeliveryPolicy;
import vroong.laas.delivery.core.domain.delivery.required.AgentService;
import vroong.laas.delivery.core.domain.delivery.required.DeliveryRepository;
import vroong.laas.delivery.core.domain.delivery.required.OrderService;
import vroong.laas.delivery.core.domain.delivery.required.SafePhoneNumberService;

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
    private final AgentService agentService;
    private final OrderService orderService;
    private final SafePhoneNumberService safePhoneNumberService;

    /**
     * 배송 생성
     *
     * <p>배차 완료 이벤트를 받아서 배송을 생성합니다.
     *
     * @param command 배송 생성 Command
     * @return 생성된 배송
     */
    @Transactional
    public Delivery create(CreateDeliveryCommand command) {
        // 1. 배송번호 생성
        DeliveryNumber deliveryNumber = deliveryNumberGenerator.generate();

        // 2. 기사 정보 조회 (더미)
        AgentInfo agentInfo = agentService.getAgentInfo(command.agentId());

        // 3. 주문 배송 정책 조회 (더미)
        OrderDeliveryPolicy orderPolicy = orderService.getDeliveryPolicy(command.orderId());

        // 4. 안심번호 생성 (더미)
        DeliverySafePhoneNumber safePhoneNumber = safePhoneNumberService.createSafePhoneNumber(
            agentInfo.phoneNumber(),
            command.orderId().toString()
        );

        // 5. 배송 정책 생성
        DeliveryPolicy policy = DeliveryPolicy.create(
            orderPolicy.alcoholDelivery(),
            orderPolicy.contactlessDelivery(),
            orderPolicy.pickupRequestTime()
        );

        // 6. 배송 생성 및 저장
        Delivery delivery = deliveryRepository.store(
            deliveryNumber,
            command.orderId(),
            command.dispatchId(),
            command.agentId(),
            command.deliveryFee(),
            safePhoneNumber,
            policy
        );

        // 7. 도메인 이벤트 발행

        return delivery;
    }


}
