package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 상점 도착 단계
 */
public class ArrivedStep extends DeliveryStep {
    
    public ArrivedStep() {
        super(DeliveryStatus.ARRIVED, "상점 도착", "상점에 도착했습니다");
    }
    
    @Override
    public void execute(Delivery delivery, DeliveryRouting routing) {
        // 상점 도착 로직
        delivery.setStatus(DeliveryStatus.ARRIVED);
        // 위치 정보 업데이트 등
    }
    
    @Override
    public void validate(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            throw new IllegalStateException("현재 라우팅에는 상점 도착 단계가 포함되어 있지 않습니다");
        }
        
        // 필수 단계인지 확인
        if (routing.isStepRequired(this)) {
            // 필수 단계는 반드시 실행되어야 함
            if (delivery.getStatus() == this.getStatus()) {
                throw new IllegalStateException("필수 단계(" + this.getStepName() + ")는 이미 완료되었습니다");
            }
        }
        
        // 이전 단계가 올바른지 확인
        DeliveryStep previousStep = routing.getPreviousStep(this);
        if (previousStep != null && delivery.getStatus() != previousStep.getStatus()) {
            throw new IllegalStateException("이전 단계(" + previousStep.getStepName() + ")를 먼저 완료해야 합니다");
        }
    }
    
    @Override
    public boolean canTransition(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            return false;
        }
        
        // 이전 단계가 완료되었는지 확인
        DeliveryStep previousStep = routing.getPreviousStep(this);
        if (previousStep != null) {
            return delivery.getStatus() == previousStep.getStatus();
        }
        
        // 첫 번째 단계인 경우
        return delivery.getStatus() == DeliveryStatus.STARTED;
    }
    
    @Override
    public DeliveryStep getNextStep(DeliveryRouting routing) {
        return routing.getNextStep(this);
    }
}
