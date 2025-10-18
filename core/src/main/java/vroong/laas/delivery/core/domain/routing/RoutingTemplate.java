package vroong.laas.delivery.core.domain.routing;

import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;

import java.util.List;

/**
 * 배송 라우팅 템플릿 (마스터 데이터)
 *
 * <p>배송 타입별 라우팅 정의를 관리합니다.
 * 각 배송은 이 템플릿을 기반으로 개별 라우팅 인스턴스를 생성합니다.
 */
public class RoutingTemplate {
    
    private final String deliveryTypeCode;
    private final String templateName;
    private final List<DeliveryStatus> statusSequence;
    
    public RoutingTemplate(
        String deliveryTypeCode, 
        String templateName, 
        List<DeliveryStatus> statusSequence
    ) {
        this.deliveryTypeCode = deliveryTypeCode;
        this.templateName = templateName;
        this.statusSequence = statusSequence;
    }
    
    /**
     * 배송 상태 시퀀스 조회
     */
    public List<DeliveryStatus> getStatusSequence() {
        return statusSequence;
    }
    
    // Getters
    public String getDeliveryTypeCode() {
        return deliveryTypeCode;
    }
    
    public String getTemplateName() {
        return templateName;
    }
}
