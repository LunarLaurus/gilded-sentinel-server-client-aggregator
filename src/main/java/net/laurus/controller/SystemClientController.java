package net.laurus.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.laurus.component.ClientManagementService;
import net.laurus.data.dto.client.BaseClientDto;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemClientController {

	@Autowired
	private ClientManagementService clientManagementService;
	
	@GetMapping("/clients")
	public Collection<BaseClientDto> getAuthenticatedClients() {
		return clientManagementService.getClientDataMap().values();
	}

	@GetMapping("/client/{clientName}")
	public ResponseEntity<BaseClientDto> getAutheticatedClientByName(@PathVariable String clientName) {
		if (clientName == null || clientName.length() <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		BaseClientDto client = clientManagementService.getClientDataMap().get(clientName);
		if (client != null) {
			return new ResponseEntity<>(client, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
