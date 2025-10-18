package vroong.laas.delivery.core.domain.delivery.exception;

import vroong.laas.delivery.core.common.exception.BaseException;
import vroong.laas.delivery.core.common.exception.ErrorCode;

/**
 * 배송을 찾을 수 없을 때 발생하는 예외
 */
public class DeliveryNotFoundException extends BaseException {

    public DeliveryNotFoundException(Long deliveryId) {
        super(ErrorCode.DELIVERY_NOT_FOUND, "배송을 찾을 수 없습니다. ID: " + deliveryId);
    }

    public DeliveryNotFoundException(String deliveryNumber) {
        super(ErrorCode.DELIVERY_NOT_FOUND, "배송을 찾을 수 없습니다. 배송번호: " + deliveryNumber);
    }
}
