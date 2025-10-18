package vroong.laas.delivery.core.domain.delivery.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.step.DeliveryStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 배송 라우팅 정보
 *
 * <p>배송 타입별 단계 정의와 상태 전이를 관리합니다.
 */
public class DeliveryRouting {
    
    private final String deliveryTypeCode;
    private final List<DeliveryStep> steps;
    private final Map<DeliveryStep, Boolean> stepRequiredMap;
    
    public DeliveryRouting(String deliveryTypeCode, List<DeliveryStep> steps) {
        this.deliveryTypeCode = deliveryTypeCode;
        this.steps = steps;
        this.stepRequiredMap = createStepRequiredMap(steps);
    }
    
    /**
     * 단계별 필수 여부를 포함한 라우팅 생성
     */
    public DeliveryRouting(String deliveryTypeCode, List<DeliveryStep> steps, Map<DeliveryStep, Boolean> stepRequirements) {
        this.deliveryTypeCode = deliveryTypeCode;
        this.steps = steps;
        this.stepRequiredMap = new HashMap<>(stepRequirements);
    }
    
    /**
     * 단계별 필수 여부 맵 생성 (기본값: 모든 단계 필수)
     */
    private Map<DeliveryStep, Boolean> createStepRequiredMap(List<DeliveryStep> steps) {
        Map<DeliveryStep, Boolean> map = new HashMap<>();
        for (DeliveryStep step : steps) {
            map.put(step, true); // 기본값: 모든 단계 필수
        }
        return map;
    }
    
    /**
     * 상태에 해당하는 단계 조회
     */
    public DeliveryStep getStep(DeliveryStatus status) {
        return steps.stream()
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
        return steps.contains(step);
    }
    
    /**
     * 특정 단계의 이전 단계 조회
     */
    public DeliveryStep getPreviousStep(DeliveryStep currentStep) {
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
        int currentIndex = steps.indexOf(currentStep);
        if (currentIndex < 0 || currentIndex >= steps.size() - 1) {
            return null; // 마지막 단계이거나 단계를 찾을 수 없음
        }
        return steps.get(currentIndex + 1);
    }
    
    /**
     * 배송 타입 코드 조회
     */
    public String getDeliveryTypeCode() {
        return deliveryTypeCode;
    }
    
    /**
     * 모든 단계 조회
     */
    public List<DeliveryStep> getSteps() {
        return steps;
    }
    
    /**
     * 특정 단계가 필수인지 확인
     */
    public boolean isStepRequired(DeliveryStep step) {
        return stepRequiredMap.getOrDefault(step, false);
    }
    
    /**
     * 특정 상태의 단계가 필수인지 확인
     */
    public boolean isStepRequired(DeliveryStatus status) {
        DeliveryStep step = getStep(status);
        return step != null && isStepRequired(step);
    }
    
    /**
     * 필수 단계들만 조회
     */
    public List<DeliveryStep> getRequiredSteps() {
        return steps.stream()
            .filter(this::isStepRequired)
            .toList();
    }
    
    /**
     * 선택적 단계들만 조회
     */
    public List<DeliveryStep> getOptionalSteps() {
        return steps.stream()
            .filter(step -> !isStepRequired(step))
            .toList();
    }
    
    /**
     * 라우팅에서 단계 건너뛰기 가능한지 확인
     */
    public boolean canSkipStep(DeliveryStep step) {
        return !isStepRequired(step);
    }
    
    /**
     * 특정 단계를 필수로 설정
     */
    public void setStepRequired(DeliveryStep step, boolean required) {
        stepRequiredMap.put(step, required);
    }
    
    /**
     * 특정 상태의 단계를 필수로 설정
     */
    public void setStepRequired(DeliveryStatus status, boolean required) {
        DeliveryStep step = getStep(status);
        if (step != null) {
            setStepRequired(step, required);
        }
    }
    
}
