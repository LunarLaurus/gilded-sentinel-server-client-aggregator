package net.laurus.dto;

import org.springframework.validation.annotation.Validated;

import lombok.Value;
import lombok.extern.java.Log;
import net.laurus.data.dto.system.SystemInfo;
import net.laurus.data.dto.system.SystemInfoDto;
import net.laurus.data.enums.Vendor;
import net.laurus.util.NetworkUtil;

@Value
@Validated
@Log
public class BaseClientDto {

	private String name;
	private Vendor vendor;
	private boolean hasIpmi;
	private SystemInfo system;

	public BaseClientDto(SystemInfoDto systemInfoDTO) {
		this.name = systemInfoDTO.getClientName();
		Vendor vendor = Vendor.lookup(systemInfoDTO.getModelName());
		log.info("Client Vendor: " + systemInfoDTO.getModelName());
		this.hasIpmi = !NetworkUtil.isZeroIPAddress(systemInfoDTO.getIloAddress());
		this.system = SystemInfo.from(systemInfoDTO);
		if (hasIpmi) {
			
		} else {
			
		}
		this.vendor = vendor;
		log.info("Created Client: " + name);
	}

}
