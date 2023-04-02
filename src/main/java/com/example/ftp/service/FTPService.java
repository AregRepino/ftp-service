package com.example.ftp.service;

import com.example.ftp.confiig.FTPConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FTPService {

    private final FTPConfig ftpConfig;
    private final FTPClient ftpClient = new FTPClient();


    public Map<String, List<FTPFile>> search(String folder, String prefix) throws IOException {
        this.open();
        Map<String, List<FTPFile>> result = this.listFiles("", folder, prefix);
        this.close();

        return result;
    }

    public Map<String, List<FTPFile>> searchByPath(String path) throws IOException {
        this.open();
        Map<String, List<FTPFile>> result = this.retrieveFile(path);
        this.close();

        return result;
    }

    public void open() throws IOException {
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        ftpClient.login(ftpConfig.getUser(), ftpConfig.getPassword());
    }

    public void close() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public List<FTPFile> findFiles(String prefix, FTPFile[] ftpFiles) {
        List<FTPFile> result = new ArrayList<>();
        for (FTPFile f : ftpFiles) {
            if (f.isFile() && f.getName().startsWith(prefix)) {
                result.add(f);
            }
        }
        return result;

    }

    public Map<String, List<FTPFile>> retrieveFile(String path) throws IOException {
        int index = path.lastIndexOf("/");
        if (index == path.length() - 1) {
            return new HashMap<>();
        }
        String folderPath = path.substring(0, index);
        String fileName = path.substring(index + 1);

        ftpClient.changeWorkingDirectory(folderPath);
        FTPFile[] ftpFiles = ftpClient.listFiles();
        List<FTPFile> files = this.findFiles(fileName, ftpFiles);

        Map<String, List<FTPFile>> map = new HashMap<>();
        map.put(folderPath, files);

        return map;
    }

    public Map<String, List<FTPFile>> listFiles(String path, String folderName, String prefix) throws IOException {
        Map<String, List<FTPFile>> map = new HashMap<>();
        ftpClient.changeWorkingDirectory(path);
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile f : ftpFiles) {
            if (f.getName().equals(".") || f.getName().equals("..")) {
                continue;
            }
            if (path.endsWith("/" + folderName)) {
                List<FTPFile> files = findFiles(prefix, ftpFiles);
                map.put(path, files);
            }
            if (f.isDirectory()) {
                String newPath = path + "/" + f.getName();
                Map<String, List<FTPFile>> files = this.listFiles(newPath, folderName, prefix);
                map.putAll(files);

            }
        }
        return map;

    }


}
