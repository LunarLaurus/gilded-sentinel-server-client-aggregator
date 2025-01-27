package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;
import static net.laurus.controller.client.LmSensorsClientController.CONTROLLER_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.lmsensors.RustClientData;
import net.laurus.service.ClientManagementService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CONTROLLER_PATH)
public class LmSensorsClientController extends AbstractClientController<RustClientData> {

    public static final String CONTROLLER_PATH = CLIENT_CONTROLLER_BASE_PATH + "/" + "sensors";

    @Autowired
    private final ClientManagementService clientManagementService;

    @Override
    protected void processClient(RustClientData clientDto) {
        log.info("Processing RustClientData: {}", clientDto);
        // Add specific business logic here if needed
    }

    @PostMapping
    public ResponseEntity<String> handleLmSensorsRequest(@RequestBody RustClientData clientDto) {
        log.info("Received lm-sensors client data at /{}/: {}", CONTROLLER_PATH, clientDto);
        clientManagementService.storeLmSensorsClient(clientDto);
        processClient(clientDto);
        return ResponseEntity.ok("lm-sensors client data stored successfully.");
    }
}
