package com.onjyb.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {
    private static final int MEGABYTE = 1048576;
    private static boolean downlaod = false;

    public interface DownloadListener {
        void onDownloadFailed(String str);

        void onDownloadSuccessful();
    }

    public static boolean downloadFile(String fileUrl, File directory, DownloadListener listener) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(fileUrl).openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();
            byte[] buffer = new byte[1048576];
            while (true) {
                int bufferLength = inputStream.read(buffer);
                if (bufferLength <= 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            if (listener != null) {
                listener.onDownloadSuccessful();
            }
            return true;
        } catch (FileNotFoundException e) {
            if (listener == null) {
                return false;
            }
            listener.onDownloadFailed(e.getLocalizedMessage());
            return false;
        } catch (MalformedURLException e2) {
            if (listener == null) {
                return false;
            }
            listener.onDownloadFailed(e2.getLocalizedMessage());
            return false;
        } catch (IOException e3) {
            if (listener == null) {
                return false;
            }
            listener.onDownloadFailed(e3.getLocalizedMessage());
            return false;
        }
    }
}
