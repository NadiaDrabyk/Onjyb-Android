package com.onjyb.reqreshelper;

import android.net.Uri;
import java.util.Arrays;

public class FileObject {
    private String accessName;
    private byte[] byteData;
    private String contentType;
    private String fileName;
    private String filePath;
    private String fileType;
    private Uri fileURI;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAccessName() {
        return this.accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getByteData() {
        return this.byteData;
    }

    public void setByteData(byte[] byteData) {
        this.byteData = byteData;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Uri getFileURI() {
        return this.fileURI;
    }

    public void setFileURI(Uri fileURI) {
        this.fileURI = fileURI;
    }

    public String toString() {
        return "FileObject [fileName=" + this.fileName + ", accessName=" + this.accessName + ", fileType=" + this.fileType + ", filePath=" + this.filePath + ", byteData=" + Arrays.toString(this.byteData) + ", contentType=" + this.contentType + "]";
    }
}
