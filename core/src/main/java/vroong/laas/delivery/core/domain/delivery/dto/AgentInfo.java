package vroong.laas.delivery.core.domain.delivery.dto;

/**
 * 기사 정보 DTO
 */
public record AgentInfo(
    Long agentId,
    String phoneNumber,
    String name
) {}

