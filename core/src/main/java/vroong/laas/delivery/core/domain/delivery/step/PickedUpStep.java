package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 픽업 완료 단계
 */
public class PickedUpStep extends DeliveryStep {
    
    public PickedUpStep() {
        super(DeliveryStatus.PICKED_UP, "픽업 완료", "픽업이 완료되었습니다");
    }
    
    @Override
    public void execute(Delivery delivery, DeliveryRouting routing) {
        delivery.setStatus(DeliveryStatus.PICKED_UP);
        // 픽업 시간 기록 등
    }
    
    @Override
    public void validate(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            throw new IllegalStateException("현재 라우팅에는 픽업 단계가 포함되어 있지 않습니다");
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
        
        // 첫 번째 단계인 경우 (간단 배송)
        return delivery.getStatus() == DeliveryStatus.STARTED;
    }
    
    @Override
    public DeliveryStep getNextStep(DeliveryRouting routing) {
        return routing.getNextStep(this);
    }
}
