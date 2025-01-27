package net.laurus.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.laurus.data.dto.client.BaseClientDto;

@Service
public class ClientQueueService {

	private final RabbitTemplate queue;

	@Autowired
	public ClientQueueService(@Lazy RabbitTemplate rabbitTemplate) {
		this.queue = rabbitTemplate;
	}

	public void updateClient(BaseClientDto client) {
		queue.convertAndSend("systemClientQueue", client);
	}

}
