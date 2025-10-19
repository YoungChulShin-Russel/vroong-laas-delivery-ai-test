package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.DeliveryRouting;

/**
 * 상점 도착 단계
 */
public class PickupArrivedStep extends DeliveryStep {
    
    public PickupArrivedStep() {
        super(DeliveryStatus.PICKUP_ARRIVED);
    }

    @Override
    protected void validateExecutionInternal(Delivery delivery, DeliveryRouting routing) {

    }

    @Override
    protected void executeInternal(Delivery delivery, DeliveryRouting routing) {
        // 상점 도착 로직
        delivery.arrive();

        // 위치 정보 업데이트 등
    }

    @Override
    protected void validateCancellationInternal(Delivery delivery, DeliveryRouting routing) {

    }

    @Override
    protected void cancelInternal(Delivery delivery, DeliveryRouting routing) {

    }
}
