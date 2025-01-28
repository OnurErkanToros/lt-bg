package org.lt.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.AbuseCheckResponseDto;
import org.lt.project.exception.customExceptions.InternalServerErrorException;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AbuseDBApiService {
    private final OkHttpClient okHttpClient;
    private final AbuseDBKeyService keyService;
    private final AbuseDBService abuseDBService;
    @Value("${abuse.api.url}")
    private String url;
    private final ObjectMapper objectMapper;

    public AbuseDBApiService(
            AbuseDBKeyService abuseDBKeyService, AbuseDBService abuseDBService) {
        this.abuseDBService = abuseDBService;
        this.okHttpClient = new OkHttpClient();
        this.keyService = abuseDBKeyService;
        this.objectMapper = new ObjectMapper();
    }

    public AbuseCheckResponseDto checkIp(int maxAgeInDays, String ipAddress) {
        final var activeKey = keyService.getLastActiveKey();
        try {
            if (maxAgeInDays == 0) {
                maxAgeInDays = 90;
            }
            String checkUrl = this.url + String.format("check?maxAgeInDays=%s&verbose&ipAddress=%s", maxAgeInDays, ipAddress);
            Request request = new Request.Builder()
                    .url(checkUrl)
                    .header("Accept", "application/json")
                    .header("key", activeKey.abuseKey())
                    .build();
            try (Response response = okHttpClient.newCall(request).execute();
                 ResponseBody responseBody = response.body()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody.string());
                if (jsonNode.has("errors")) {
                    throw new ResourceNotFoundException(jsonNode.get("errors").asText());
                }
                JsonNode jsonNodeData = jsonNode.get("data");
                AbuseCheckResponseDto responseDto = objectMapper.readValue(jsonNodeData.toString(), AbuseCheckResponseDto.class);
                if (responseDto == null) {
                    throw new ResourceNotFoundException("Abuse sorgulaması ile ilgili bir sorun var.");
                }
                return responseDto;
            } catch (Exception e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<AbuseBlackListResponseDto> getBlackList() {
        final var activeKey = keyService.getLastActiveKey();
        try {
            String blackListUrl = url + "blacklist";
            Request request = new Request.Builder()
                    .url(blackListUrl)
                    .header("Accept", "application/json")
                    .header("key", activeKey.abuseKey())
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String json = responseBody.string();
                JsonNode allNode = objectMapper.readTree(json);
                if (allNode.has("data")) {
                    JsonNode dataNode = allNode.get("data");
                    List<AbuseBlackListResponseDto> list = Arrays.asList(objectMapper.readValue(dataNode.toString(), AbuseBlackListResponseDto[].class));
                    responseBody.close();
                    response.close();
                    return list;
                } else if (allNode.has("errors")) {
                    JsonNode errorsNode = objectMapper.readTree(json).get("errors").get(0).get("detail");
                    throw new ResourceNotFoundException(errorsNode.asText());
                } else {
                    throw new ResourceNotFoundException("Abuse ile ilgili bir sorun var.");
                }

            }
            throw new ResourceNotFoundException("blacklist response boş");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
