package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 배송 단계 추상 클래스
 *
 * <p>각 배송 단계의 공통 인터페이스를 정의합니다.
 * 하위 클래스에서 execute, validate 등의 메서드를 구현합니다.
 */
public abstract class DeliveryStep {
    
    protected final DeliveryStatus status;
    protected final String stepName;
    protected final String description;
    
    public DeliveryStep(DeliveryStatus status, String stepName, String description) {
        this.status = status;
        this.stepName = stepName;
        this.description = description;
    }
    
    /**
     * 단계 실행
     * 
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     */
    public abstract void execute(Delivery delivery, DeliveryRouting routing);
    
    /**
     * 단계 검증
     * 
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @throws IllegalStateException 검증 실패 시
     */
    public abstract void validate(Delivery delivery, DeliveryRouting routing);
    
    /**
     * 다음 단계로 이동 가능한지 확인
     * 
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @return 이동 가능 여부
     */
    public abstract boolean canTransition(Delivery delivery, DeliveryRouting routing);
    
    /**
     * 다음 단계 조회
     * 
     * @param routing 배송 라우팅 정보
     * @return 다음 단계 (마지막 단계면 null)
     */
    public abstract DeliveryStep getNextStep(DeliveryRouting routing);
    
    // Getters
    public DeliveryStatus getStatus() {
        return status;
    }
    
    public String getStepName() {
        return stepName;
    }
    
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", stepName, status);
    }
}
