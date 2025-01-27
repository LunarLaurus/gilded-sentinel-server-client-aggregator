package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;
import static net.laurus.controller.client.EsxiClientController.CONTROLLER_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.esxi.EsxiSystemDataDto;
import net.laurus.service.ClientManagementService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CONTROLLER_PATH)
public class EsxiClientController extends AbstractClientController<EsxiSystemDataDto> {

    public static final String CONTROLLER_PATH = CLIENT_CONTROLLER_BASE_PATH + "/" + "esxi";

    @Autowired
    private final ClientManagementService clientManagementService;

    @Override
    protected void processClient(EsxiSystemDataDto clientDto) {
        log.info("Processing EsxiSystemDataDto: {}", clientDto);
        // Add specific business logic here if needed
    }

    @PostMapping
    public ResponseEntity<String> handleEsxiRequest(@RequestBody EsxiSystemDataDto clientDto) {
        log.info("Received ESXi client data at /{}/: {}", CONTROLLER_PATH, clientDto);
        clientManagementService.storeEsxiClient(clientDto);
        processClient(clientDto);
        return ResponseEntity.ok("ESXi client data stored successfully.");
    }
}
