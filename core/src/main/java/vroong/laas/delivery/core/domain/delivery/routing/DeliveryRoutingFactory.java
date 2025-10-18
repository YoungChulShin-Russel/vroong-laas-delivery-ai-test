package vroong.laas.delivery.core.domain.delivery.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.step.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 배송 라우팅 팩토리
 *
 * <p>API를 통해 동적으로 배송 라우팅을 생성합니다.
 */
public class DeliveryRoutingFactory {
    
    /**
     * 라우팅 생성 요청 DTO
     */
    public record CreateRoutingRequest(
        String routingName,
        List<DeliveryStatus> statusSequence
    ) {}
    
    /**
     * API로 라우팅 생성
     * 
     * @param request 라우팅 생성 요청
     * @return 생성된 라우팅
     */
    public static DeliveryRouting createRouting(CreateRoutingRequest request) {
        List<DeliveryStep> steps = createStepsFromStatusSequence(request.statusSequence());
        return new DeliveryRouting(request.routingName(), steps);
    }
    
    /**
     * 라우팅 생성 요청 DTO (단계별 필수 여부 포함)
     */
    public record CreateRoutingWithRequirementsRequest(
        String routingName,
        List<DeliveryStatus> statusSequence,
        Map<DeliveryStatus, Boolean> stepRequirements  // 각 상태별 필수 여부
    ) {}
    
    /**
     * 단계별 필수 여부를 포함한 라우팅 생성
     */
    public static DeliveryRouting createRoutingWithRequirements(CreateRoutingWithRequirementsRequest request) {
        List<DeliveryStep> steps = createStepsFromStatusSequence(request.statusSequence());
        Map<DeliveryStep, Boolean> stepRequirements = createStepRequirementsMap(steps, request.stepRequirements());
        
        return new DeliveryRouting(request.routingName(), steps, stepRequirements);
    }
    
    /**
     * 단계별 필수 여부 맵 생성
     */
    private static Map<DeliveryStep, Boolean> createStepRequirementsMap(
        List<DeliveryStep> steps, 
        Map<DeliveryStatus, Boolean> statusRequirements
    ) {
        Map<DeliveryStep, Boolean> stepRequirements = new HashMap<>();
        
        for (DeliveryStep step : steps) {
            // 상태별 필수 여부가 설정되어 있으면 사용, 없으면 기본값(true)
            boolean isRequired = statusRequirements.getOrDefault(step.getStatus(), true);
            stepRequirements.put(step, isRequired);
        }
        
        return stepRequirements;
    }
    
    /**
     * 상태 시퀀스로 단계들 생성
     */
    private static List<DeliveryStep> createStepsFromStatusSequence(List<DeliveryStatus> statusSequence) {
        List<DeliveryStep> steps = new ArrayList<>();
        
        for (DeliveryStatus status : statusSequence) {
            DeliveryStep step = createStepByStatus(status);
            if (step != null) {
                steps.add(step);
            }
        }
        
        return steps;
    }
    
    /**
     * 라우팅 빌더
     */
    public static class DeliveryRoutingBuilder {
        private final String deliveryTypeCode;
        private final List<DeliveryStep> steps = new ArrayList<>();
        private final Map<DeliveryStep, Boolean> stepRequirements = new HashMap<>();
        
        public DeliveryRoutingBuilder(String deliveryTypeCode) {
            this.deliveryTypeCode = deliveryTypeCode;
        }
        
        /**
         * 단계 추가 (기본값: 필수)
         */
        public DeliveryRoutingBuilder addStep(DeliveryStep step) {
            return addStep(step, true);
        }
        
        /**
         * 단계 추가 (필수 여부 지정)
         */
        public DeliveryRoutingBuilder addStep(DeliveryStep step, boolean required) {
            steps.add(step);
            stepRequirements.put(step, required);
            return this;
        }
        
        /**
         * 상태로 단계 추가 (기본값: 필수)
         */
        public DeliveryRoutingBuilder addStep(DeliveryStatus status) {
            return addStep(status, true);
        }
        
        /**
         * 상태로 단계 추가 (필수 여부 지정)
         */
        public DeliveryRoutingBuilder addStep(DeliveryStatus status, boolean required) {
            DeliveryStep step = createStepByStatus(status);
            if (step != null) {
                return addStep(step, required);
            }
            return this;
        }
        
        /**
         * 라우팅 빌드
         */
        public DeliveryRouting build() {
            return new DeliveryRouting(deliveryTypeCode, steps, stepRequirements);
        }
    }
    
    /**
     * 라우팅 빌더 생성
     */
    public static DeliveryRoutingBuilder builder(String deliveryTypeCode) {
        return new DeliveryRoutingBuilder(deliveryTypeCode);
    }
    
    /**
     * 상태별 단계 생성
     */
    private static DeliveryStep createStepByStatus(DeliveryStatus status) {
        return switch (status) {
            case STARTED -> new StartedStep();
            case ARRIVED -> new ArrivedStep();
            case PICKED_UP -> new PickedUpStep();
            case PHOTO_UPLOAD -> new PhotoUploadStep();
            case COMPLETED -> new CompletedStep();
            case CANCELLED -> null; // 취소는 별도 처리
        };
    }
    
}
