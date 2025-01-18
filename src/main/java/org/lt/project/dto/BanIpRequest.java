package org.lt.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.lt.project.model.BanningIp;

@Builder
public record BanIpRequest(String ip, BanningIp.BanningIpType ipType) {
}
