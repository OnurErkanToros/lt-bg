package org.lt.project.core.convertor;

import org.lt.project.dto.SuspectIpDto;
import org.lt.project.entity.SuspectIPEntity;

public class SuspectIpConverter {
    public static SuspectIPEntity convert(SuspectIpDto suspectIpDto){
        SuspectIPEntity suspectIPEntity = new SuspectIPEntity();
        suspectIPEntity.setIpAddress(suspectIpDto.getIp());
        suspectIPEntity.setWhichHost(suspectIpDto.getWhichHost());
        suspectIPEntity.setAccessForbiddenNumber(suspectIpDto.getAccessForbiddenNumber());
        suspectIPEntity.setLine(suspectIPEntity.getLine());
        return suspectIPEntity;
    }
    public static SuspectIpDto convert(SuspectIPEntity suspectIPEntity){
        SuspectIpDto suspectIpDto = new SuspectIpDto();
        suspectIpDto.setIp(suspectIPEntity.getIpAddress());
        suspectIpDto.setWhichHost(suspectIPEntity.getWhichHost());
        suspectIpDto.setAccessForbiddenNumber(suspectIPEntity.getAccessForbiddenNumber());
        suspectIpDto.setLine(suspectIPEntity.getLine());
        return suspectIpDto;
    }
}
