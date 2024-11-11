package net.laurus.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketDTO {
    
    @JsonAlias(value = "ClientName")
    private String clientName;
    @JsonAlias(value = "CpuCount")
    private int cpuCount;
    @JsonAlias(value = "CpuTemperatures")
    private Map<String, Map<String, Float>> cpuTemperatures;
    @JsonAlias(value = "IloAddress")
    private String iloAddress;    
    
}
