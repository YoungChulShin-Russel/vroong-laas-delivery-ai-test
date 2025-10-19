package vroong.laas.delivery.core.domain.delivery.command;

import lombok.Builder;

import vroong.laas.delivery.core.domain.delivery.DispatchResult;

/**
 * 배송 생성 Command
 *
 * <p>배차 완료 이벤트에서 배송을 생성할 때 사용됩니다.
 */
@Builder
public record CreateDeliveryCommand(
    DispatchResult dispatchResult
) {
    /**
     * 필수 값 검증
     */
    public CreateDeliveryCommand {
        if (dispatchResult == null) {
            throw new IllegalArgumentException("배차 정보는 필수입니다");
        }
    }
}
