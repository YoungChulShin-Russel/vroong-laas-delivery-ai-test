package vroong.laas.delivery.core.domain.routing;

import lombok.Getter;

import java.util.List;

/**
 * 배송 라우팅 템플릿 (마스터 데이터)
 *
 * <p>배송 타입별 라우팅 정의를 관리합니다.
 * 각 배송은 이 템플릿을 기반으로 개별 라우팅 인스턴스를 생성합니다.
 */
@Getter
public class RoutingTemplate {
    
    private final String routingCode;
    private final String description;
    private final List<RoutingStatusSequence> statusSequence;
    
    public RoutingTemplate(
        String routingCode,
        String description,
        List<RoutingStatusSequence> statusSequences
    ) {
        if (routingCode == null || routingCode.isEmpty()) {
            throw new IllegalArgumentException("routingCode is null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("description is null or empty");
        }
        if (statusSequences == null || statusSequences.isEmpty()) {
            throw new IllegalArgumentException("statusSequence is null or empty");
        }
        if (statusSequences.stream()
            .filter(status -> !status.required())
            .anyMatch(status -> status.deliveryStatus().isRequired())) {
            throw new IllegalArgumentException("Invalid required value");
        }

        this.routingCode = routingCode;
        this.description = description;
        this.statusSequence = statusSequences;
    }
}
