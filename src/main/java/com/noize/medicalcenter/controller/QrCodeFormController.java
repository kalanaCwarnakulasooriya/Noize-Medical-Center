package com.noize.medicalcenter.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class QrCodeFormController {
    public String getQrAPI(String userID){
        String qrAPI = "http://api.qrserver.com/v1/";
        String qrSize = "300x300";
        return qrAPI + "create-qr-code/?size=" + qrSize + "&data=" + userID;
    }
    public static String readQRCodeFromFile(String filePath) {
        String responseData = null;
        String url = "http://api.qrserver.com/v1/read-qr-code/";

        try {
            File file = new File(filePath);
            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(("--" + boundary + CRLF).getBytes());
                output.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + CRLF).getBytes());
                output.write(("Content-Type: image/png" + CRLF).getBytes());
                output.write(CRLF.getBytes());

                try (FileInputStream input = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                output.write(CRLF.getBytes());
                output.write(("--" + boundary + "--" + CRLF).getBytes());
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                String responseData1 = response.toString();
                JSONArray jsonArray = new JSONArray(responseData1);

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONArray symbolArray = jsonObject.getJSONArray("symbol");
                JSONObject symbolObject = symbolArray.getJSONObject(0);
                System.out.println("Symbol Object: " + symbolObject);
                try {
                    responseData = symbolObject.getString("data");
                } catch (Exception e) {
                    System.out.println("Error: " + symbolObject.getString("error"));
                }
                System.out.println("Extracted Data: " + responseData);
            } else {
                System.out.println("Failed to read QR Code. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
