package com.example.ftp.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class FTPFileDto {
    private String path;
    private Instant date;
    private long size;
}
