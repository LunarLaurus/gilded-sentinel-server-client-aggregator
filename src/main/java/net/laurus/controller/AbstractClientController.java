package net.laurus.controller;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laurus.service.ClientManagementService;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractClientController<T> {

    @Autowired
    @Getter
    private final ClientManagementService clientManagementService;
	
	public abstract String getTypeOfClient();

	public String getControllerPath() {
		return CLIENT_CONTROLLER_BASE_PATH + "/" + getTypeOfClient();
	}
	
    // Abstract method to process the specific DTO type
    protected abstract void processClient(T clientDto);

    // Endpoint method shared by all subclasses
    @PostMapping
    public final ResponseEntity<String> handleClientRequest(@RequestBody T clientDto) {
        try {
            log.info("Received "+getTypeOfClient()+" client data at /{}/: {}", getControllerPath(), clientDto);
            log.info("Processing {}: {}",clientDto.getClass().getSimpleName(), clientDto);
            processClient(clientDto);
            return ResponseEntity.ok("Request processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}
