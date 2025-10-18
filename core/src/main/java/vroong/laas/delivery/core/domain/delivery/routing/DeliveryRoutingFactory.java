package vroong.laas.delivery.core.domain.delivery.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.step.*;
import vroong.laas.delivery.core.domain.routing.RoutingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 배송 라우팅 팩토리
 *
 * <p>템플릿 기반으로 개별 배송용 라우팅을 생성합니다.
 */
public class DeliveryRoutingFactory {
    
    /**
     * 템플릿 생성 요청 DTO
     */
    public record CreateTemplateRequest(
        String deliveryTypeCode,
        String templateName,
        List<DeliveryStatus> statusSequence
    ) {}
    
    /**
     * 템플릿 생성 요청 DTO (단계별 필수 여부 포함)
     */
    public record CreateTemplateWithRequirementsRequest(
        String deliveryTypeCode,
        String templateName,
        List<DeliveryStatus> statusSequence,
        Map<DeliveryStatus, Boolean> stepRequirements
    ) {}
    
    /**
     * 템플릿 생성
     * 
     * @param request 템플릿 생성 요청
     * @return 생성된 템플릿
     */
    public static RoutingTemplate createTemplate(CreateTemplateRequest request) {
        return new RoutingTemplate(
            request.deliveryTypeCode(),
            request.templateName(),
            request.statusSequence()
        );
    }
    
    /**
     * 템플릿 생성 (단계별 필수 여부 포함)
     * 
     * @param request 템플릿 생성 요청
     * @return 생성된 템플릿
     */
    public static RoutingTemplate createTemplateWithRequirements(CreateTemplateWithRequirementsRequest request) {
        return new RoutingTemplate(
            request.deliveryTypeCode(),
            request.templateName(),
            request.statusSequence()
        );
    }
    
    /**
     * 개별 배송용 라우팅 생성
     * 
     * @param template 템플릿
     * @param deliveryId 배송 ID
     * @return 개별 배송용 라우팅
     */
    public static DeliveryRouting createRoutingForDelivery(RoutingTemplate template, Long deliveryId) {
        // RoutingTemplate의 statusSequence를 기반으로 DeliveryRoutingStep 생성
        List<DeliveryStep> steps = createStepsFromStatusSequence(template.getStatusSequence());
        List<DeliveryRoutingStep> routingSteps = createDefaultRoutingSteps(steps);
        
        return new DeliveryRouting(
            deliveryId,
            template.getDeliveryTypeCode(),
            template.getTemplateName(),
            routingSteps
        );
    }
    
    /**
     * 기본 라우팅 단계 생성 (모든 단계 필수)
     */
    private static List<DeliveryRoutingStep> createDefaultRoutingSteps(List<DeliveryStep> steps) {
        return steps.stream()
            .map(step -> new DeliveryRoutingStep(step, true))
            .toList();
    }
    
    /**
     * 상태별 필수 여부 맵을 라우팅 단계로 변환
     */
    private static List<DeliveryRoutingStep> createRoutingStepsFromStatusMap(
        List<DeliveryStep> steps, 
        Map<DeliveryStatus, Boolean> statusRequirements
    ) {
        return steps.stream()
            .map(step -> {
                boolean isRequired = statusRequirements.getOrDefault(step.getStatus(), true);
                return new DeliveryRoutingStep(step, isRequired);
            })
            .toList();
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
        private final List<DeliveryRoutingStep> routingSteps = new ArrayList<>();
        
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
            routingSteps.add(new DeliveryRoutingStep(step, required));
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
         * 라우팅 빌드 (개별 배송용)
         */
        public DeliveryRouting build(Long deliveryId) {
            return new DeliveryRouting(deliveryId, deliveryTypeCode, "CUSTOM", routingSteps);
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
