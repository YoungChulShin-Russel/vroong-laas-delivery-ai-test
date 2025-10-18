package vroong.laas.delivery.core.domain.delivery.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.step.DeliveryStep;

import java.util.List;

/**
 * 개별 배송용 라우팅 인스턴스
 *
 * <p>특정 배송의 단계 정의와 상태 전이를 관리합니다.
 * DeliveryRoutingTemplate을 기반으로 생성됩니다.
 */
public class DeliveryRouting {
    
    private final Long deliveryId;
    private final String deliveryTypeCode;
    private final String templateName;
    private final List<DeliveryRoutingStep> routingSteps;
    
    /**
     * 개별 배송용 라우팅 생성
     */
    public DeliveryRouting(
        Long deliveryId,
        String deliveryTypeCode,
        String templateName,
        List<DeliveryRoutingStep> routingSteps
    ) {
        this.deliveryId = deliveryId;
        this.deliveryTypeCode = deliveryTypeCode;
        this.templateName = templateName;
        this.routingSteps = routingSteps;
    }
    
    // Getters
    public Long getDeliveryId() {
        return deliveryId;
    }
    
    public String getDeliveryTypeCode() {
        return deliveryTypeCode;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public List<DeliveryRoutingStep> getRoutingSteps() {
        return routingSteps;
    }
    
    /**
     * 단계 목록 조회 (하위 호환성)
     */
    public List<DeliveryStep> getSteps() {
        return routingSteps.stream()
            .map(DeliveryRoutingStep::getStep)
            .toList();
    }
    
    /**
     * 상태에 해당하는 단계 조회
     */
    public DeliveryStep getStep(DeliveryStatus status) {
        return routingSteps.stream()
            .map(DeliveryRoutingStep::getStep)
            .filter(step -> step.getStatus() == status)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * 현재 상태에서 다음 단계 조회
     * 
     * <p>선택적 단계는 건너뛸 수 있습니다.
     */
    public DeliveryStep getNextStep(DeliveryStatus currentStatus) {
        DeliveryStep currentStep = getStep(currentStatus);
        if (currentStep == null) {
            return null;
        }
        
        DeliveryStep nextStep = currentStep.getNextStep(this);
        
        // 다음 단계가 선택적이면 건너뛰고 다음 필수 단계 찾기
        while (nextStep != null && canSkipStep(nextStep)) {
            nextStep = nextStep.getNextStep(this);
        }
        
        return nextStep;
    }
    
    /**
     * 현재 단계에서 다음 단계로 이동 가능한지 확인
     * 
     * <p>선택적 단계는 건너뛸 수 있습니다.
     */
    public boolean canMoveToNext(DeliveryStatus currentStatus, DeliveryStatus nextStatus) {
        DeliveryStep currentStep = getStep(currentStatus);
        DeliveryStep nextStep = getStep(nextStatus);
        
        if (currentStep == null || nextStep == null) {
            return false;
        }
        
        // 직접 다음 단계인 경우
        if (currentStep.getNextStep(this) == nextStep) {
            return true;
        }
        
        // 선택적 단계들을 건너뛰고 다음 단계로 이동 가능한지 확인
        return canSkipToNextStep(currentStep, nextStep);
    }
    
    /**
     * 선택적 단계들을 건너뛰고 다음 단계로 이동 가능한지 확인
     */
    private boolean canSkipToNextStep(DeliveryStep currentStep, DeliveryStep targetStep) {
        DeliveryStep nextStep = currentStep.getNextStep(this);
        
        while (nextStep != null && nextStep != targetStep) {
            // 선택적 단계가 아니면 건너뛸 수 없음
            if (isStepRequired(nextStep)) {
                return false;
            }
            
            nextStep = nextStep.getNextStep(this);
        }
        
        return nextStep == targetStep;
    }
    
    /**
     * 특정 단계가 라우팅에 포함되어 있는지 확인
     */
    public boolean containsStep(DeliveryStep step) {
        return routingSteps.stream()
            .anyMatch(routingStep -> routingStep.getStep().equals(step));
    }
    
    /**
     * 특정 단계의 이전 단계 조회
     */
    public DeliveryStep getPreviousStep(DeliveryStep currentStep) {
        List<DeliveryStep> steps = getSteps();
        int currentIndex = steps.indexOf(currentStep);
        if (currentIndex <= 0) {
            return null; // 첫 번째 단계이거나 단계를 찾을 수 없음
        }
        return steps.get(currentIndex - 1);
    }
    
    /**
     * 특정 단계의 다음 단계 조회
     */
    public DeliveryStep getNextStep(DeliveryStep currentStep) {
        List<DeliveryStep> steps = getSteps();
        int currentIndex = steps.indexOf(currentStep);
        if (currentIndex < 0 || currentIndex >= steps.size() - 1) {
            return null; // 마지막 단계이거나 단계를 찾을 수 없음
        }
        return steps.get(currentIndex + 1);
    }
    
    
    /**
     * 특정 단계가 필수인지 확인
     */
    public boolean isStepRequired(DeliveryStep step) {
        return routingSteps.stream()
            .filter(routingStep -> routingStep.getStep().equals(step))
            .findFirst()
            .map(DeliveryRoutingStep::isRequired)
            .orElse(false);
    }
    
    /**
     * 특정 상태의 단계가 필수인지 확인
     */
    public boolean isStepRequired(DeliveryStatus status) {
        return routingSteps.stream()
            .filter(routingStep -> routingStep.getStep().getStatus() == status)
            .findFirst()
            .map(DeliveryRoutingStep::isRequired)
            .orElse(false);
    }
    
    /**
     * 필수 단계들만 조회
     */
    public List<DeliveryStep> getRequiredSteps() {
        return routingSteps.stream()
            .filter(DeliveryRoutingStep::isRequired)
            .map(DeliveryRoutingStep::getStep)
            .toList();
    }
    
    /**
     * 선택적 단계들만 조회
     */
    public List<DeliveryStep> getOptionalSteps() {
        return routingSteps.stream()
            .filter(DeliveryRoutingStep::isOptional)
            .map(DeliveryRoutingStep::getStep)
            .toList();
    }
    
    /**
     * 라우팅에서 단계 건너뛰기 가능한지 확인
     */
    public boolean canSkipStep(DeliveryStep step) {
        return !isStepRequired(step);
    }
    
    // 개별 배송용 라우팅은 수정할 수 없음 (템플릿에서 생성된 불변 객체)
    
}
