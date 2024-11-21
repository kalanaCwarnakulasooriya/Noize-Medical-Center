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
        //filePath = "C:\\Users\\User_101\\Pictures\\Camera Roll\\WIN_20241023_12_16_31_Pro.jpg";
        String url = "http://api.qrserver.com/v1/read-qr-code/";

        try {
            File file = new File(filePath);
            String boundary = Long.toHexString(System.currentTimeMillis()); // Unique boundary for the request
            String CRLF = "\r\n"; // Line separator

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Write the file part
            try (OutputStream output = connection.getOutputStream()) {
                output.write(("--" + boundary + CRLF).getBytes());
                output.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + CRLF).getBytes());
                output.write(("Content-Type: image/png" + CRLF).getBytes()); // Change based on your image type
                output.write(CRLF.getBytes());

                // Read the file and write to output
                try (FileInputStream input = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                output.write(CRLF.getBytes()); // End of the file part
                output.write(("--" + boundary + "--" + CRLF).getBytes()); // End of the request
            }

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
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


                // Print the extracted data
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
