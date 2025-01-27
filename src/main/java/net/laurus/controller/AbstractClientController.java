package net.laurus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractClientController<T> {
	
    // Abstract method to process the specific DTO type
    protected abstract void processClient(T clientDto);

    // Endpoint method shared by all subclasses
    public final ResponseEntity<String> handleRequest(@RequestBody T clientDto) {
        try {
            processClient(clientDto);
            return ResponseEntity.ok("Request processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}
