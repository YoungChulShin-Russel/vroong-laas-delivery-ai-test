package vroong.laas.delivery.core.domain.delivery.exception;

import vroong.laas.delivery.core.common.exception.BaseException;
import vroong.laas.delivery.core.common.exception.ErrorCode;

/**
 * 배송 상태 변경이 유효하지 않을 때 발생하는 예외
 */
public class InvalidDeliveryException extends BaseException {

    public InvalidDeliveryException(String message) {
        super(ErrorCode.INVALID_DELIVERY, message);
    }

    public InvalidDeliveryException(String message, Throwable cause) {
        super(ErrorCode.INVALID_DELIVERY, message, cause);
    }
}
