package net.laurus.controller.client;

import static net.laurus.Constants.CLIENT_CONTROLLER_BASE_PATH;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.laurus.controller.AbstractClientController;
import net.laurus.data.dto.system.esxi.EsxiSystemDataDto;
import net.laurus.service.ClientManagementService;

@RestController
@RequestMapping(CLIENT_CONTROLLER_BASE_PATH + "/esxi")
public class EsxiClientController extends AbstractClientController<EsxiSystemDataDto> {

    public EsxiClientController(ClientManagementService clientManagementService) {
		super(clientManagementService);
	}

	@Override
    protected void processClient(EsxiSystemDataDto clientDto) {
        getClientManagementService().storeEsxiClient(clientDto);
    }

	@Override
	public String getTypeOfClient() {
		return "esxi";
	}
}
