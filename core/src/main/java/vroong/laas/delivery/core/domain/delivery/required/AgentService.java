package vroong.laas.delivery.core.domain.delivery.required;

import vroong.laas.delivery.core.domain.delivery.dto.AgentInfo;

/**
 * 기사 서비스 인터페이스
 */
public interface AgentService {
    AgentInfo getAgentInfo(Long agentId);
}

