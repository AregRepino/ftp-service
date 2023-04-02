package com.example.ftp.mapper;

import com.example.ftp.dto.FTPFileDto;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FTPFileMapper {

    public List<FTPFileDto> toFtpFileDtos(Map<String, List<FTPFile>> listMap) {
        List<FTPFileDto> dtoList = new ArrayList<>();

        for (Map.Entry<String, List<FTPFile>> pair : listMap.entrySet()) {
            String path = pair.getKey();
            List<FTPFile> files = pair.getValue();
            for (FTPFile file : files) {
                FTPFileDto ftpFileDto = new FTPFileDto();
                ftpFileDto.setPath(path + "/" + file.getName());
                ftpFileDto.setDate(file.getTimestamp().toInstant());
                ftpFileDto.setSize(file.getSize());
                dtoList.add(ftpFileDto);
            }
        }
        return dtoList;
    }
}
