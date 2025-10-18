package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 배송 시작 단계
 */
public class StartedStep extends DeliveryStep {
    
    public StartedStep() {
        super(DeliveryStatus.STARTED, "배송 시작", "배송이 시작되었습니다");
    }
    
    @Override
    public void execute(Delivery delivery, DeliveryRouting routing) {
        // 배송 시작 로직
        delivery.setStatus(DeliveryStatus.STARTED);
        // 안심번호 활성화 등
    }
    
    @Override
    public void validate(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            throw new IllegalStateException("현재 라우팅에는 배송 시작 단계가 포함되어 있지 않습니다");
        }
        
        if (delivery.getStatus() != null) {
            throw new IllegalStateException("이미 시작된 배송입니다");
        }
    }
    
    @Override
    public boolean canTransition(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            return false;
        }
        
        return delivery.getStatus() == null; // 최초 상태
    }
    
    @Override
    public DeliveryStep getNextStep(DeliveryRouting routing) {
        return routing.getNextStep(this);
    }
}
