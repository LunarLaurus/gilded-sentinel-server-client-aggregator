package net.laurus.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.laurus.data.dto.system.esxi.EsxiSystemDataDto;
import net.laurus.data.dto.system.lmsensors.RustClientData;
import net.laurus.data.dto.system.librehw.SystemInfoDto;

@Slf4j
@Service
public class ClientManagementService {

    private final AtomicBoolean healthy = new AtomicBoolean(false);

    @Getter
    private final Map<String, EsxiSystemDataDto> esxiClients = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, RustClientData> lmSensorsClients = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, SystemInfoDto> librehardwareClients = new ConcurrentHashMap<>();

    @PostConstruct
    public void start() {
        healthy.set(true);
        log.info("ClientManagementService started. Ready to aggregate and hold client data.");
    }

    @PreDestroy
    public void stop() {
        healthy.set(false);
        log.info("ClientManagementService stopped.");
    }

    public void storeEsxiClient(EsxiSystemDataDto clientDto) {
        esxiClients.put(clientDto.getSystem().getHostname(), clientDto);
        log.info("Stored ESXi client data: {}", clientDto.getSystem().getHostname());
    }

    public void storeLmSensorsClient(RustClientData clientDto) {
        lmSensorsClients.put(clientDto.getHostName(), clientDto);
        log.info("Stored lm-sensors client data: {}", clientDto.getHostName());
    }

    public void storeLibreHardwareClient(SystemInfoDto clientDto) {
        librehardwareClients.put(clientDto.getSystemHostName(), clientDto);
        log.info("Stored LibreHardware client data: {}", clientDto.getSystemHostName());
    }

    public boolean isHealthy() {
        return healthy.get();
    }
}
