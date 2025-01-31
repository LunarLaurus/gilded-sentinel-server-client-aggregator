package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.librehw.SystemInfoDto;
import net.laurus.service.ClientManagementService;

@RestController
@RequestMapping(CLIENT_CONTROLLER_BASE_PATH + "/librehardware")
public class LibreHardwareClientController extends AbstractClientController<SystemInfoDto> {

    public LibreHardwareClientController(ClientManagementService clientManagementService) {
		super(clientManagementService);
	}

    @Override
    protected void processClient(SystemInfoDto clientDto) {
        getClientManagementService().storeLibreHardwareClient(clientDto);
    }

	@Override
	public String getTypeOfClient() {
		return "librehardware";
	}
    
}
