package vroong.laas.delivery.core.domain.delivery.required;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryNumber;

import java.util.Optional;
import vroong.laas.delivery.core.domain.delivery.NewDelivery;

/**
 * 배송 Repository Port
 *
 * <p>배송의 영속성을 관리하는 인터페이스입니다.
 *
 * <p>Infrastructure Layer에서 구현됩니다.
 */
public interface DeliveryRepository {

    Delivery save(NewDelivery newDelivery);
}
