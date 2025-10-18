package vroong.laas.delivery.core.domain.delivery.required;

import vroong.laas.delivery.core.domain.delivery.DeliverySafePhoneNumber;

/**
 * 안심번호 서비스 인터페이스
 */
public interface SafePhoneNumberService {
    DeliverySafePhoneNumber createSafePhoneNumber(String originalPhoneNumber, String orderId);
}

