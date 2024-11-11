package net.laurus.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.java.Log;
import net.laurus.Constant;
import net.laurus.data.dto.system.SystemInfoDto;
import net.laurus.dto.BaseClientDto;

@Log
@Component
public class ClientHandler implements Runnable {
	
	private boolean healthy = false;

    @Getter
    private final Map<String, BaseClientDto> clientDataMap = new ConcurrentHashMap<>();

    private final ExecutorService threadPool;

    @Value("${system.socket-port}")
    private int port;
    
    @Autowired
    public ClientHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @PostConstruct
    public void start() {
        new Thread(this, "Socket-Handler").start(); // Start the SocketHandler in a separate thread
        healthy = true;           
    }

    @PreDestroy
    public void stop() {
        threadPool.shutdown(); // Shutdown the thread pool when the application is stopped
        healthy = false;           
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            // Deserialize JSON to SystemInfoDTO object
            String jsonData = reader.readLine();
            log.info(jsonData);
            SystemInfoDto systemInfoDTO = Constant.JSON_MAPPER.readValue(jsonData, SystemInfoDto.class);

            try {
                BaseClientDto client = clientDataMap.computeIfAbsent(systemInfoDTO.getClientName(),
                        key -> generateClient(systemInfoDTO));    
                writer.println("OK-200");
                healthy = true;           
                log.info(client.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            log.severe("Error handling client: " + e.getMessage());
        }
    }

    private static BaseClientDto generateClient(SystemInfoDto systemInfoDTO) {
        return new BaseClientDto(systemInfoDTO);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Socket Listener started. Waiting for clients on port " + port + "...");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(socket));
                } catch (IOException e) {
                    log.severe("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            log.severe("Server error: " + e.getMessage());
        }
    }

	public boolean isHealthy() {
		return healthy;
	}
}
