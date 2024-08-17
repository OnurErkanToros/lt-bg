package org.lt.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.AbuseCheckResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.ErrorDataResult;
import org.lt.project.dto.resultDto.SuccessDataResult;
import org.lt.project.model.AbuseDBCheckLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
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

    public DataResult<AbuseCheckResponseDto> checkIp(int maxAgeInDays, String ipAddress) {
        try {
            if (maxAgeInDays == 0) {
                maxAgeInDays = 90;
            }
            String checkUrl = this.url + String.format("check?maxAgeInDays=%s&verbose&ipAddress=%s", maxAgeInDays, ipAddress);
            Request request = new Request.Builder()
                    .url(checkUrl)
                    .header("Accept", "application/json")
                    .header("key", keyService.getLastActiveKey().getData().abuseKey())
                    .build();
            try (Response response = okHttpClient.newCall(request).execute();
                 ResponseBody responseBody = response.body()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody.string()).get("data");
                AbuseCheckResponseDto responseDto = objectMapper.readValue(jsonNode.toString(), AbuseCheckResponseDto.class);
                if (responseDto != null) {
                    String username = UserService.getAuthenticatedUser();
                    abuseDBService.addAbuseLog(
                            AbuseDBCheckLog.builder()
                                    .ipAddress(responseDto.ipAddress())
                                    .usageType(responseDto.usageType())
                                    .ipVersion(responseDto.ipVersion())
                                    .numDistinctUsers(responseDto.numDistinctUsers())
                                    .abuseConfidenceScore(responseDto.abuseConfidenceScore())
                                    .isp(responseDto.isp())
                                    .countryCode(responseDto.countryCode())
                                    .countryName(responseDto.countryName())
                                    .domain(responseDto.domain())
                                    .isTor(responseDto.isTor())
                                    .isBanned(false)
                                    .isPublic(responseDto.isPublic())
                                    .isWhiteListed(responseDto.isWhitelisted())
                                    .checkBy(username)
                                    .checkDate(new Date())
                                    .lastReportedAt(responseDto.lastReportedAt())
                                    .build()
                    );
                }
                return new SuccessDataResult<>(responseDto);
            }catch (Exception e){
                return new ErrorDataResult<>(e.getMessage());
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(e.getMessage());
        }
    }

    public DataResult<List<AbuseBlackListResponseDto>> getBlackList() {
        try {
            String blackListUrl = this.url + "blacklist";
            Request request = new Request.Builder()
                    .url(blackListUrl)
                    .header("Accept", "application/json")
                    .header("key", keyService.getLastActiveKey().getData().abuseKey())
                    .build();   
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String json = responseBody.string();
                JsonNode allNode = objectMapper.readTree(json);
                if(allNode.has("data")){
                    JsonNode dataNode = allNode.get("data");
                    List<AbuseBlackListResponseDto> list = Arrays.asList(objectMapper.readValue(dataNode.toString(), AbuseBlackListResponseDto[].class));
                    responseBody.close();
                    response.close();
                    return new SuccessDataResult<>(list);
                }else if(allNode.has("errors")){
                    JsonNode errorsNode = objectMapper.readTree(json).get("errors").get(0).get("detail");
                    return new ErrorDataResult<>(errorsNode.asText());
                }else {
                    return new ErrorDataResult<>("Abuse ile ilgili bir sorun var.");
                }

            }
        } catch (Exception e) {
            return new ErrorDataResult<>(e.getMessage());
        }
        return new ErrorDataResult<>();
    }
}
