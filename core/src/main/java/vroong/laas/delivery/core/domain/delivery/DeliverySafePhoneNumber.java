package vroong.laas.delivery.core.domain.delivery;

import lombok.Getter;

import java.time.Instant;

/**
 * 배송 안심번호 Value Object
 *
 * <p>기사와 고객 간 안심번호를 관리합니다.
 */
@Getter
public class DeliverySafePhoneNumber {

    private final String safePhoneNumber;
    private final String originalPhoneNumber;
    private final String orderId;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private final Instant expiredAt;
    private final SafePhoneNumberStatus status;

    public DeliverySafePhoneNumber(
        String safePhoneNumber,
        String originalPhoneNumber,
        String orderId,
        Instant issuedAt,
        Instant expiresAt,
        Instant expiredAt,
        SafePhoneNumberStatus status
    ) {
        if (safePhoneNumber == null || safePhoneNumber.isBlank()) {
            throw new IllegalArgumentException("안심번호는 필수입니다");
        }
        if (originalPhoneNumber == null || originalPhoneNumber.isBlank()) {
            throw new IllegalArgumentException("원본 전화번호는 필수입니다");
        }
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다");
        }
        if (issuedAt == null) {
            throw new IllegalArgumentException("발급 시간은 필수입니다");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("만료 시간은 필수입니다");
        }
        if (status == null) {
            throw new IllegalArgumentException("상태는 필수입니다");
        }

        this.safePhoneNumber = safePhoneNumber;
        this.originalPhoneNumber = originalPhoneNumber;
        this.orderId = orderId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.expiredAt = expiredAt;
        this.status = status;
    }

    /**
     * 안심번호 생성
     */
    public static DeliverySafePhoneNumber create(
        String safePhoneNumber,
        String originalPhoneNumber,
        String orderId,
        Instant issuedAt,
        Instant expiresAt
    ) {
        return new DeliverySafePhoneNumber(
            safePhoneNumber,
            originalPhoneNumber,
            orderId,
            issuedAt,
            expiresAt,
            null, // expiredAt은 나중에 설정
            SafePhoneNumberStatus.ACTIVE
        );
    }


    /**
     * 안심번호 상태 Enum
     */
    public enum SafePhoneNumberStatus {
        ACTIVE("활성"),
        EXPIRED("만료"),
        CANCELLED("취소");

        private final String description;

        SafePhoneNumberStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
