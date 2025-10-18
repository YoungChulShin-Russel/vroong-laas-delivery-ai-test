package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;
import lombok.ToString;
import vroong.laas.delivery.core.domain.shared.AggregateRoot;
import vroong.laas.delivery.core.domain.delivery.event.DeliveryCreatedEvent;
import vroong.laas.delivery.core.domain.delivery.event.DeliveryStatusChangedEvent;
import vroong.laas.delivery.core.domain.delivery.step.DeliveryStep;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 배송 Aggregate Root
 *
 * <p>배송의 핵심 비즈니스 로직과 상태를 관리합니다.
 *
 * <p>상태 전이:
 * - 정상: STARTED → ARRIVED → PICKED_UP → COMPLETED
 * - 취소: STARTED → CANCELLED, ARRIVED → CANCELLED
 */
@Getter
@ToString
public class Delivery extends AggregateRoot {

    private final Long id;
    private final DeliveryNumber deliveryNumber;
    private final Long orderId;
    private final Long dispatchId;
    private final Long agentId;
    private final BigDecimal deliveryFee;
    private DeliveryStatus status;
    private final DeliverySafePhoneNumber safePhoneNumber;
    private final DeliveryPolicy policy;
    private final List<DeliveryStatusHistory> statusHistories;
    private final List<DeliveryPhoto> photos;
    private final Instant createdAt;
    private final Instant updatedAt;
    
    // 배송 단계 관련 필드
    private final String deliveryTypeCode;
    private DeliveryStep currentStep;
    private DeliveryStep nextStep;

    // 생성자 - 필수 값 검증
    public Delivery(
        Long id,
        DeliveryNumber deliveryNumber,
        Long orderId,
        Long dispatchId,
        Long agentId,
        BigDecimal deliveryFee,
        DeliveryStatus status,
        DeliverySafePhoneNumber safePhoneNumber,
        DeliveryPolicy policy,
        List<DeliveryStatusHistory> statusHistories,
        List<DeliveryPhoto> photos,
        Instant createdAt,
        Instant updatedAt,
        String deliveryTypeCode,
        DeliveryStep currentStep,
        DeliveryStep nextStep
    ) {
        // 필수 값 검증
        if (id == null) {
            throw new IllegalArgumentException("ID는 필수입니다");
        }
        if (deliveryNumber == null) {
            throw new IllegalArgumentException("배송번호는 필수입니다");
        }
        if (orderId == null) {
            throw new IllegalArgumentException("주문 ID는 필수입니다");
        }
        if (dispatchId == null) {
            throw new IllegalArgumentException("배차 ID는 필수입니다");
        }
        if (agentId == null) {
            throw new IllegalArgumentException("기사 ID는 필수입니다");
        }
        if (deliveryFee == null || deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다");
        }
        if (status == null) {
            throw new IllegalArgumentException("배송 상태는 필수입니다");
        }
        if (safePhoneNumber == null) {
            throw new IllegalArgumentException("안심번호는 필수입니다");
        }
        if (policy == null) {
            throw new IllegalArgumentException("배송 정책은 필수입니다");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("생성 시간은 필수입니다");
        }

        this.id = id;
        this.deliveryNumber = deliveryNumber;
        this.orderId = orderId;
        this.dispatchId = dispatchId;
        this.agentId = agentId;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.safePhoneNumber = safePhoneNumber;
        this.policy = policy;
        this.statusHistories = statusHistories != null ? new ArrayList<>(statusHistories) : new ArrayList<>();
        this.photos = photos != null ? new ArrayList<>(photos) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deliveryTypeCode = deliveryTypeCode;
        this.currentStep = currentStep;
        this.nextStep = nextStep;
    }

    /**
     * 배송 생성 팩토리 메서드
     *
     * <p>배송을 생성하고 초기 상태 이력을 기록합니다.
     */
    public static Delivery create(
        Long id,
        DeliveryNumber deliveryNumber,
        Long orderId,
        Long dispatchId,
        Long agentId,
        BigDecimal deliveryFee,
        DeliverySafePhoneNumber safePhoneNumber,
        DeliveryPolicy policy
    ) {
        Instant now = Instant.now();
        
        Delivery delivery = new Delivery(
            id,
            deliveryNumber,
            orderId,
            dispatchId,
            agentId,
            deliveryFee,
            DeliveryStatus.STARTED,
            safePhoneNumber,
            policy,
            new ArrayList<>(),
            new ArrayList<>(),
            now,
            now
        );

        // 초기 상태 이력 추가
        delivery.addStatusHistory(DeliveryStatus.STARTED, "배송 시작", now);
        
        // 도메인 이벤트 추가
        delivery.addDomainEvent(DeliveryCreatedEvent.from(delivery));

        return delivery;
    }

    /**
     * 상점 도착
     */
    public void arrive() {
        if (status != DeliveryStatus.STARTED) {
            throw new IllegalStateException("배송 시작 상태에서만 상점 도착이 가능합니다");
        }

        changeStatus(DeliveryStatus.ARRIVED, "상점 도착");
    }

    /**
     * 픽업 완료
     */
    public void pickup() {
        if (status != DeliveryStatus.ARRIVED) {
            throw new IllegalStateException("상점 도착 상태에서만 픽업이 가능합니다");
        }

        changeStatus(DeliveryStatus.PICKED_UP, "픽업 완료");
    }

    /**
     * 배송 완료
     */
    public void complete() {
        if (status != DeliveryStatus.PICKED_UP) {
            throw new IllegalStateException("픽업 완료 상태에서만 배송 완료가 가능합니다");
        }

        changeStatus(DeliveryStatus.COMPLETED, "배송 완료");
    }

    /**
     * 배송 취소
     */
    public void cancel(String reason) {
        if (status == DeliveryStatus.PICKED_UP || status == DeliveryStatus.COMPLETED) {
            throw new IllegalStateException("픽업 완료 후에는 취소할 수 없습니다");
        }

        changeStatus(DeliveryStatus.CANCELLED, "배송 취소: " + reason);
    }


    /**
     * 상태 변경
     */
    private void changeStatus(DeliveryStatus newStatus, String reason) {
        if (this.status == newStatus) {
            return;
        }

        addStatusHistory(newStatus, reason, Instant.now());
        
        // 도메인 이벤트 추가
        addDomainEvent(DeliveryStatusChangedEvent.from(this, newStatus, reason));
    }

    /**
     * 상태 이력 추가
     */
    private void addStatusHistory(DeliveryStatus status, String reason, Instant changedAt) {
        DeliveryStatusHistory history = new DeliveryStatusHistory(
            null, // ID는 저장 시 할당
            this.id,
            status,
            "PROGRESS",
            reason,
            changedAt
        );
        this.statusHistories.add(history);
    }

    /**
     * 취소 가능 여부 확인
     */
    public boolean isCancellable() {
        return status == DeliveryStatus.STARTED || status == DeliveryStatus.ARRIVED;
    }
    
    /**
     * 상태 설정 (내부용)
     */
    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
    
    /**
     * 현재 단계 조회
     */
    public DeliveryStep getCurrentStep() {
        return currentStep;
    }
    
    /**
     * 다음 단계 조회
     */
    public DeliveryStep getNextStep() {
        return nextStep;
    }
    
    /**
     * 다음 단계로 이동
     */
    public void moveToNextStep() {
        if (nextStep != null) {
            nextStep.validate(this);
            nextStep.execute(this);
            this.currentStep = nextStep;
            this.nextStep = currentStep.getNextStep();
        }
    }
    
    /**
     * 다음 단계로 이동 가능한지 확인
     */
    public boolean canMoveToNext() {
        return nextStep != null && nextStep.canTransition(this);
    }
    
    /**
     * 사진 필수 여부 확인
     */
    public boolean isPhotoRequired() {
        return "PHOTO_REQUIRED".equals(deliveryTypeCode);
    }
}
