package vroong.laas.delivery.core.domain.delivery.step;

import vroong.laas.delivery.core.domain.delivery.Delivery;
import vroong.laas.delivery.core.domain.delivery.DeliveryStatus;
import vroong.laas.delivery.core.domain.delivery.routing.DeliveryRouting;

/**
 * 사진 업로드 단계 (선택적)
 */
public class PhotoUploadStep extends DeliveryStep {
    
    public PhotoUploadStep() {
        super(DeliveryStatus.PHOTO_UPLOAD, "사진 업로드", "배송 사진을 업로드해야 합니다");
    }
    
    @Override
    public void execute(Delivery delivery, DeliveryRouting routing) {
        delivery.setStatus(DeliveryStatus.PHOTO_UPLOAD);
        // 사진 업로드 로직
    }
    
    @Override
    public void validate(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            throw new IllegalStateException("현재 라우팅에는 사진 업로드 단계가 포함되어 있지 않습니다");
        }
        
        // 선택적 단계이므로 건너뛸 수 있는지 확인
        if (routing.canSkipStep(this)) {
            // 선택적 단계는 건너뛸 수 있음 (다음 단계로 진행 가능)
            return;
        }
        
        // 필수 단계인 경우 이전 단계 확인
        DeliveryStep previousStep = routing.getPreviousStep(this);
        if (previousStep != null && delivery.getStatus() != previousStep.getStatus()) {
            throw new IllegalStateException("이전 단계(" + previousStep.getStepName() + ")를 먼저 완료해야 합니다");
        }
        
        // 사진이 업로드되었는지 확인 (실제 구현에서는 delivery.getPhotos() 체크)
        // if (delivery.getPhotos().isEmpty()) {
        //     throw new IllegalStateException("배송 사진이 필요합니다");
        // }
    }
    
    @Override
    public boolean canTransition(Delivery delivery, DeliveryRouting routing) {
        // 라우팅에 이 단계가 포함되어 있는지 확인
        if (!routing.containsStep(this)) {
            return false;
        }
        
        // 이전 단계가 완료되었는지 확인
        DeliveryStep previousStep = routing.getPreviousStep(this);
        if (previousStep != null) {
            return delivery.getStatus() == previousStep.getStatus();
        }
        
        return false; // 사진 업로드는 항상 이전 단계가 있어야 함
    }
    
    @Override
    public DeliveryStep getNextStep(DeliveryRouting routing) {
        return routing.getNextStep(this);
    }
}
