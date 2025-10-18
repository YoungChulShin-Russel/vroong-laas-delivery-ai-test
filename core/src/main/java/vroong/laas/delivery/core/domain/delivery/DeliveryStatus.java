package vroong.laas.delivery.core.domain.delivery;

/**
 * 배송 상태 Enum
 *
 * <p>배송의 진행 상태를 나타냅니다.
 *
 * <p>상태 전이:
 * - 정상 진행: STARTED → ARRIVED → PICKED_UP → COMPLETED
 * - 취소: STARTED → CANCELLED, ARRIVED → CANCELLED
 */
public enum DeliveryStatus {
    
    /** 배송 시작 */
    STARTED("배송 시작"),
    
    /** 상점 도착 */
    ARRIVED("상점 도착"),
    
    /** 픽업 완료 */
    PICKED_UP("픽업 완료"),
    
    /** 사진 업로드 */
    PHOTO_UPLOAD("사진 업로드"),
    
    /** 배송 완료 */
    COMPLETED("배송 완료"),
    
    /** 배송 취소 */
    CANCELLED("배송 취소");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 취소 가능한 상태인지 확인
     */
    public boolean isCancellable() {
        return this == STARTED || this == ARRIVED;
    }

    /**
     * 완료된 상태인지 확인
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }

    /**
     * 취소된 상태인지 확인
     */
    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
