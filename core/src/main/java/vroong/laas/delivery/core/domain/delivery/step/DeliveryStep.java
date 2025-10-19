package vroong.laas.delivery.core.domain.delivery.step;

import lombok.Getter;
import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.DeliveryRouting;

/**
 * 배송 단계 추상 클래스
 *
 * <p>각 배송 단계의 공통 인터페이스를 정의합니다.
 * 하위 클래스에서 execute, validate 등의 메서드를 구현합니다.
 */
@Getter
public abstract class DeliveryStep {
    
    protected final DeliveryStatus status;

    public DeliveryStep(DeliveryStatus status) {
        this.status = status;
    }

    /**
     * 단계 실행
     * 
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     */
    public void execute(Delivery delivery, DeliveryRouting routing) {
        validateExecution(delivery, routing);
        validateExecutionInternal(delivery, routing);

        executeInternal(delivery, routing);
    }

    private void validateExecution(Delivery delivery, DeliveryRouting routing) {
        if (!routing.containsStatus(status)) {
            throw new IllegalStateException(
                String.format("현재 라우팅에는 %s 단계가 포함되어 있지 않습니다", status.getDescription()));
        }

        if (canTransition(delivery, routing)) {
            throw new IllegalStateException(
                String.format("현재 라우팅에는 %s 단계를 수행할 수 없습니다", status.getDescription()));
        }

    }
    /**
     * 단계 실행 검증
     *
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @throws IllegalStateException 검증 실패 시
     */
    protected abstract void validateExecutionInternal(Delivery delivery, DeliveryRouting routing);

    protected abstract void executeInternal(Delivery delivery, DeliveryRouting routing);

    /**
     * 단계 취소
     *
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     */
    public void cancel(Delivery delivery, DeliveryRouting routing) {
        validateCancellation(delivery, routing);
        validateCancellationInternal(delivery, routing);

        cancelInternal(delivery, routing);
    }


    /**
     * 단계 취소 검증
     *
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @throws IllegalStateException 검증 실패 시
     */
    private void validateCancellation(Delivery delivery, DeliveryRouting routing) {

    }

    /**
     * 단계 취소 검증
     *
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @throws IllegalStateException 검증 실패 시
     */
    protected abstract void validateCancellationInternal(Delivery delivery, DeliveryRouting routing);

    protected abstract void cancelInternal(Delivery delivery, DeliveryRouting routing);
    
    /**
     * 다음 단계로 이동 가능한지 확인
     * 
     * @param delivery 배송 객체
     * @param routing 배송 라우팅 정보
     * @return 이동 가능 여부
     */
    public boolean canTransition(Delivery delivery, DeliveryRouting routing) {
        return routing.canTransition(delivery.getStatus(), this.status);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", status.getDescription(), status);
    }
}
