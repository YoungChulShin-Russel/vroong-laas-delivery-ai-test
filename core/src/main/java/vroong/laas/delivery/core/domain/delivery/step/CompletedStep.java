package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 배송 완료 단계
 */
public class CompletedStep extends DeliveryStep {
    
    public CompletedStep() {
        super(DeliveryStatus.COMPLETED, "배송 완료", "배송이 완료되었습니다");
    }
    
    @Override
    public void execute(Delivery delivery, DeliveryRouting routing) {
        delivery.setStatus(DeliveryStatus.COMPLETED);
        // 배송 완료 로직
        // 안심번호 비활성화 등
    }
    
    @Override
    public void validate(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            throw new IllegalStateException("현재 라우팅에는 배송 완료 단계가 포함되어 있지 않습니다");
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
        
        return false; // 완료는 항상 이전 단계가 있어야 함
    }
    
    @Override
    public DeliveryStep getNextStep(DeliveryRouting routing) {
        return null; // 마지막 단계
    }
}
