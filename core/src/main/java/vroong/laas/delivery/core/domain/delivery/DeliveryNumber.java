package vroong.laas.delivery.core.domain.delivery;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 배송번호 Value Object
 *
 * <p>배송번호 형식: DEL-YYMMDDHHMMSS + 랜덤 3자리
 * 예시: DEL-250112143000123
 */
@Getter
@EqualsAndHashCode
public class DeliveryNumber {

    private final String value;
    private static final String PREFIX = "DEL-";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public DeliveryNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("배송번호는 필수입니다");
        }
        if (!value.startsWith(PREFIX)) {
            throw new IllegalArgumentException("배송번호는 " + PREFIX + "로 시작해야 합니다");
        }
        if (value.length() != 19) { // DEL- + 12자리 + 3자리 = 19자리
            throw new IllegalArgumentException("배송번호 형식이 올바르지 않습니다");
        }

        this.value = value;
    }

    /**
     * 새로운 배송번호 생성
     */
    public static DeliveryNumber generate() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(FORMATTER);
        String random = String.format("%03d", new Random().nextInt(1000));
        
        return new DeliveryNumber(PREFIX + timestamp + random);
    }

    /**
     * 기존 배송번호로 생성
     */
    public static DeliveryNumber of(String value) {
        return new DeliveryNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
