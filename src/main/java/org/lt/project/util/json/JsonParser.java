package org.lt.project.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lt.project.exception.customExceptions.InternalServerErrorException;
import org.springframework.stereotype.Component;

@Component
public class JsonParser {
    private final ObjectMapper objectMapper = new ObjectMapper();
  public String jsonToIp(String json) {
    if (json == null || json.isBlank()) {
      throw new RuntimeException("Json boş geldi.");
    }
    try{
        JsonNode node = objectMapper.readTree(json);
        return node.get("remote_addr").asText();
    }catch (Exception e){
        throw new InternalServerErrorException(e.getMessage());
    }
  }
}
