package vroong.laas.delivery.core.application.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vroong.laas.delivery.core.domain.delivery.*;
import vroong.laas.delivery.core.domain.delivery.command.CreateDeliveryCommand;
import vroong.laas.delivery.core.domain.delivery.dto.AgentInfo;
import vroong.laas.delivery.core.domain.delivery.required.*;

/**
 * 배송 Facade
 *
 * <p>배송 관련 비즈니스 흐름을 조정합니다.
 * 외부 서비스 호출 후 Domain Service를 호출합니다.
 */
@Component
@RequiredArgsConstructor
public class DeliveryFacade {

    private final DeliveryCreator deliveryCreator;

    /**
     * 배송 생성 (배차 완료 이벤트 기반)
     *
     * <p>1. 외부 서비스 호출 (주문, 기사, 안심번호)
     * 2. DeliveryCreator Domain Service 호출
     *
     * @param command 배송 생성 Command (배차 이벤트 정보)
     * @return 생성된 배송
     */
    public Delivery createDelivery(CreateDeliveryCommand command) {
        // 1. 주문 정보 조회 (외부 서비스 - 주문 서버)

        // 2. 기사 정보 조회 (외부 서비스 - 기사 서버)

        // 3. 안심번호 생성 (외부 서비스 - 통신사)

        // 4. 배송 생성 (Domain Service)
        return deliveryCreator.create(command.dispatchResult());
    }
}
