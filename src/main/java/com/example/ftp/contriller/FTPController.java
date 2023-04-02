package com.example.ftp.contriller;

import com.example.ftp.dto.FTPFileDto;
import com.example.ftp.mapper.FTPFileMapper;
import com.example.ftp.service.FTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FTPController {
    private final FTPService ftpService;

    private final FTPFileMapper ftpFileMapper;


    @GetMapping("/photos")
    public List<FTPFileDto> getPhotos() throws IOException {

        return ftpFileMapper.toFtpFileDtos(ftpService.search("фотографии", "GRP327_"));

    }

    @GetMapping("/photo")
    public List<FTPFileDto> getPhotos(@RequestParam("path") String path) throws IOException {

        return ftpFileMapper.toFtpFileDtos(ftpService.searchByPath(path));

    }
}
