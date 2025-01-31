package net.laurus.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.laurus.data.dto.system.ClientCommonSystemDto;
import net.laurus.data.dto.system.esxi.EsxiSystemDataDto;
import net.laurus.data.dto.system.librehw.SystemInfoDto;
import net.laurus.data.dto.system.lmsensors.RustClientData;
import net.laurus.queue.ClientQueueDispatcher;

@Slf4j
@Service
public class ClientManagementService {

    private static final int SCAN_RATE_MS = 1000 * 60 * 5; // 5 minutes
    
    private static final int SCAN_INITIAL_DELAY_MS = 1000 * 5; // 5 seconds
    
    @Autowired
    private ClientQueueDispatcher queue;

    @Getter
    private final Map<String, EsxiSystemDataDto> esxiClients = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, RustClientData> lmSensorsClients = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, SystemInfoDto> librehardwareClients = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, ClientCommonSystemDto> commonClients = new ConcurrentHashMap<>();

    @PostConstruct
    public void start() {
        log.info("ClientManagementService started. Ready to aggregate and hold client data.");
    }

    @PreDestroy
    public void stop() {
        log.info("ClientManagementService stopped.");
    }

    public void storeEsxiClient(EsxiSystemDataDto clientDto) {
        esxiClients.put(clientDto.getSystem().getHostname(), clientDto);
        log.info("Stored ESXi client data: {}", clientDto.getSystem().getHostname());
        storeCommonClient(clientDto);
    }

    public void storeLmSensorsClient(RustClientData clientDto) {
        lmSensorsClients.put(clientDto.getSystemInfo().getHostname(), clientDto);
        log.info("Stored lm-sensors client data: {}", clientDto.getSystemInfo().getHostname());
        storeCommonClient(clientDto);
    }

    public void storeLibreHardwareClient(SystemInfoDto clientDto) {
        librehardwareClients.put(clientDto.getSystemHostName(), clientDto);
        log.info("Stored LibreHardware client data: {}", clientDto.getSystemHostName());
    }

    private void storeCommonClient(EsxiSystemDataDto clientDto) {
    	storeCommonClient(ClientCommonSystemDto.from(clientDto));
    }

    private void storeCommonClient(RustClientData clientDto) {  
    	storeCommonClient(ClientCommonSystemDto.from(clientDto));
    }

    private void storeCommonClient(ClientCommonSystemDto common) {
    	commonClients.put(common.getHostname(), common);
        log.info("Stored common client data: {}", common);
        queue.sendClientData(common);
    }

    @Scheduled(fixedRate = SCAN_RATE_MS, initialDelay = SCAN_INITIAL_DELAY_MS)
    public void logClients() {
    	log.info("Outputting saved client list.");
    	esxiClients.values().forEach(c -> log.info("Esxi Client: {}", c));
    	lmSensorsClients.values().forEach(c -> log.info("Rust Client: {}", c));
    	librehardwareClients.values().forEach(c -> log.info("LibreHW Client: {}", c));
    	log.info("Common clients");
    	commonClients.values().forEach(c -> log.info("Client: {}", c));
    }
}
