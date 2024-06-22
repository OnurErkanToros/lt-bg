package org.lt.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.lt.project.core.result.DataResult;
import org.lt.project.core.result.ErrorDataResult;
import org.lt.project.core.result.SuccessDataResult;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.AbuseCheckResponseDto;
import org.lt.project.entity.AbuseDBLogEntity;
import org.lt.project.entity.AllSuspectIpEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AbuseDBApiService {
    private final OkHttpClient okHttpClient;
    private final LtValueService valueService;
    private final AbuseDBKeyService keyService;
    private final AbuseDBService abuseDBService;
    private final AllSuspectIpService allSuspectIpService;
    private String url;
    private final ObjectMapper objectMapper;

    public AbuseDBApiService(
            LtValueService ltValueService,
            AbuseDBKeyService abuseDBKeyService, AbuseDBService abuseDBService, AllSuspectIpService allSuspectIpService) {
        this.abuseDBService = abuseDBService;
        this.allSuspectIpService = allSuspectIpService;
        this.okHttpClient = new OkHttpClient();
        this.valueService = ltValueService;
        this.keyService = abuseDBKeyService;
        this.objectMapper = new ObjectMapper();
    }

    public DataResult<AbuseCheckResponseDto> checkIp(int maxAgeInDays, String ipAddress) {
        try {
            url = valueService.getValueByKey("abuseUrl").getMyValue();
            if (maxAgeInDays == 0) {
                maxAgeInDays = 90;
            }
            String checkUrl = this.url + String.format("check?maxAgeInDays=%s&verbose&ipAddress=%s", maxAgeInDays, ipAddress);
            Request request = new Request.Builder()
                    .url(checkUrl)
                    .header("Accept", "application/json")
                    .header("key", keyService.getLastActiveKey().getData().getAbuseKey())
                    .build();
            try (Response response = okHttpClient.newCall(request).execute();
                 ResponseBody responseBody = response.body()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody.string()).get("data");
                AbuseCheckResponseDto responseDto = objectMapper.readValue(jsonNode.toString(), AbuseCheckResponseDto.class);
                if (responseDto != null) {
                    allSuspectIpService.addSuspectIp(new AllSuspectIpEntity(responseDto.getIpAddress(), "check", new Date(), false));
                    abuseDBService.addAbuseLog(new AbuseDBLogEntity(
                            responseDto.getIpAddress(),
                            responseDto.getIsPublic(),
                            responseDto.getIpVersion(),
                            responseDto.getIsWhitelisted(),
                            responseDto.getAbuseConfidenceScore(),
                            responseDto.getCountryCode(),
                            responseDto.getCountryName(),
                            responseDto.getUsageType(),
                            responseDto.getIsp(),
                            responseDto.getDomain(),
                            responseDto.getIsTor(),
                            responseDto.getTotalReports(),
                            responseDto.getNumDistinctUsers(),
                            responseDto.getLastReportedAt()));
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
            url = valueService.getValueByKey("abuseUrl").getMyValue();
            String blackListUrl = this.url + "blacklist";
            Request request = new Request.Builder()
                    .url(blackListUrl)
                    .header("Accept", "application/json")
                    .header("key", keyService.getLastActiveKey().getData().getAbuseKey())
                    .build();   
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String json = responseBody.string();
                JsonNode dataNode = objectMapper.readTree(json).get("data");
                JsonNode errorsNode = objectMapper.readTree(json).get("errors").get(0).get("detail");
                if(errorsNode==null && dataNode!=null){
                    List<AbuseBlackListResponseDto> list = Arrays.asList(objectMapper.readValue(dataNode.toString(), AbuseBlackListResponseDto[].class));
                    responseBody.close();
                    response.close();
                    return new SuccessDataResult<>(list);
                } else if (errorsNode!=null) {
                    return new ErrorDataResult<>(errorsNode.asText());
                }else{
                    return new ErrorDataResult<>("Abuse ile ilgili bir sorun var.");
                }

            }
        } catch (Exception e) {
            return new ErrorDataResult<>(e.getMessage());
        }
        return new ErrorDataResult<>();
    }
}
