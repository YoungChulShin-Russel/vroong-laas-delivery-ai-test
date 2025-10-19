package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryRouting;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;

public class DeliverStep extends DeliveryStep {

  public DeliverStep() {
    super(DeliveryStatus.DELIVERED);
  }

  @Override
  protected void validateExecutionInternal(Delivery delivery, DeliveryRouting routing) {

  }

  @Override
  protected void executeInternal(Delivery delivery, DeliveryRouting routing) {
    delivery.deliver();
  }

  @Override
  protected void validateCancellationInternal(Delivery delivery, DeliveryRouting routing) {

  }

  @Override
  protected void cancelInternal(Delivery delivery, DeliveryRouting routing) {

  }
}
