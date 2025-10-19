package vroong.laas.delivery.core.domain.delivery;

import vroong.laas.delivery.core.domain.delivery.step.DeliveryStep;

/**
 * 배송 라우팅 단계
 *
 * <p>DeliveryStep과 필수 여부를 묶어서 관리합니다.
 */
public class DeliveryRoutingStep {
    
    private final DeliveryStep step;
    private final boolean required;
    
    public DeliveryRoutingStep(DeliveryStep step, boolean required) {
        this.step = step;
        this.required = required;
    }
    
    public DeliveryStep getStep() {
        return step;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public boolean isOptional() {
        return !required;
    }
    
    @Override
    public String toString() {
        return "DeliveryRoutingStep{" +
                "step=" + step.getStatus() +
                ", required=" + required +
                '}';
    }
}
