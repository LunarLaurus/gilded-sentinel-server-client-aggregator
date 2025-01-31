package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.lmsensors.RustClientData;
import net.laurus.service.ClientManagementService;

@RestController
@RequestMapping(CLIENT_CONTROLLER_BASE_PATH + "/sensors")
public class LmSensorsClientController extends AbstractClientController<RustClientData> {

    public LmSensorsClientController(ClientManagementService clientManagementService) {
		super(clientManagementService);
	}

    @Override
    protected void processClient(RustClientData clientDto) {
        getClientManagementService().storeLmSensorsClient(clientDto);
    }

	@Override
	public String getTypeOfClient() {
		return "sensors";
	}
    
}
