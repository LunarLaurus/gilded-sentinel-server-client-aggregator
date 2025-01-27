package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;
import static net.laurus.controller.client.LibreHardwareClientController.CONTROLLER_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.librehw.SystemInfoDto;
import net.laurus.service.ClientManagementService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CONTROLLER_PATH)
public class LibreHardwareClientController extends AbstractClientController<SystemInfoDto> {

    public static final String CONTROLLER_PATH = CLIENT_CONTROLLER_BASE_PATH + "/" + "librehardware";

    @Autowired
    private final ClientManagementService clientManagementService;

    @Override
    protected void processClient(SystemInfoDto clientDto) {
        log.info("Processing SystemInfoDto: {}", clientDto);
        // Add specific business logic here if needed
    }

    @PostMapping
    public ResponseEntity<String> handleLibreHardwareRequest(@RequestBody SystemInfoDto clientDto) {
        log.info("Received LibreHardware client data at /{}/: {}", CONTROLLER_PATH, clientDto);
        clientManagementService.storeLibreHardwareClient(clientDto);
        processClient(clientDto);
        return ResponseEntity.ok("LibreHardware client data stored successfully.");
    }
}
