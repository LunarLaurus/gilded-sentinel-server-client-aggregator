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
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.java.Log;
import net.laurus.Constant;
import net.laurus.data.dto.client.BaseClientDto;
import net.laurus.data.dto.system.SystemInfoDto;

@Log
@Service
public class ClientManagementService implements Runnable {

    // Use AtomicBoolean to ensure thread-safety when checking and modifying the healthy flag
    private final AtomicBoolean healthy = new AtomicBoolean(false);

    @Getter
    private final Map<String, BaseClientDto> clientDataMap = new ConcurrentHashMap<>();

    private final ExecutorService threadPool;
    
    private final ClientQueueService queueService;

    @Value("${system.socket-port}")
    private int port;

    @Autowired
    public ClientManagementService(ExecutorService threadPool, ClientQueueService queueService) {
        this.threadPool = threadPool;
        this.queueService = queueService;
    }

    @PostConstruct
    public void start() {
        healthy.set(true);
        threadPool.submit(this);
        log.info("Socket Listener started. Waiting for clients on port " + port + "...");
    }

    @PreDestroy
    public void stop() {
        threadPool.shutdown();
        healthy.set(false);
        log.info("Socket Listener stopped.");
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String jsonData = reader.readLine(); // Read client data
            if (jsonData != null && !jsonData.isEmpty()) {
                log.info("Received data: " + jsonData);
                try {
                    SystemInfoDto systemInfoDTO = Constant.JSON_MAPPER.readValue(jsonData, SystemInfoDto.class);

                    // Use computeIfAbsent to add or update client data
                    BaseClientDto client = clientDataMap.computeIfAbsent(systemInfoDTO.getSystemHostName(),
                            key -> generateClient(systemInfoDTO)); // Generate new client if absent
                    client.update(systemInfoDTO); // Update the client with new data
                    queueService.updateClient(client);
                    writer.println("OK-200"); // Acknowledge the client
                    log.info("Updated client: " + client);
                } catch (Exception e) {
                    log.severe("Error processing data: " + e.getMessage());
                    writer.println("Error-500"); // Internal Server Error on failure
                }
            } else {
                log.warning("Received empty or null data from client.");
                writer.println("Error-400"); // Bad Request
            }
        } catch (IOException e) {
            log.severe("Error handling client connection: " + e.getMessage());
        }
    }

    private static BaseClientDto generateClient(SystemInfoDto systemInfoDTO) {
        return new BaseClientDto(systemInfoDTO);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            healthy.set(true); // Mark server as healthy when the listener starts
            while (healthy.get()) { // Keep running as long as the server is healthy
                try {
                    Socket socket = serverSocket.accept(); // Accept client connection
                    log.info("Client connected from: " + socket.getInetAddress());
                    threadPool.submit(() -> handleClient(socket)); // Handle each client connection asynchronously
                } catch (IOException e) {
                    log.severe("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            log.severe("Server error: " + e.getMessage());
            healthy.set(false); // Mark the server as unhealthy
        }
    }

    public boolean isHealthy() {
        return healthy.get();
    }
}
