package com.noize.medicalcenter.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import java.io.IOException;

public class SendGmail {
    public static void sendEmail(String email, String otp) {
        String apiKey = System.getenv("RESEND_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API key is missing. Please set RESEND_API_KEY in environment variables.");
            return;
        }

        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("noizehealthcare@punylk.me")
                .to(email)
                .subject("Your Noize Health Care Account OTP: " + otp)
                .html("<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "    <title>OTP Verification - Noize Health Care</title>" +
                        "    <style>" +
                        "        body {" +
                        "            font-family: Arial, sans-serif;" +
                        "            background-color: #f0f4f8;" +
                        "            margin: 0;" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .email-container {" +
                        "            background-color: #ffffff;" +
                        "            margin: 0 auto;" +
                        "            padding: 20px;" +
                        "            border-radius: 8px;" +
                        "            max-width: 600px;" +
                        "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
                        "        }" +
                        "        .email-header {" +
                        "            background-color: #0073e6;" +
                        "            color: white;" +
                        "            padding: 10px;" +
                        "            border-radius: 8px 8px 0 0;" +
                        "            text-align: center;" +
                        "        }" +
                        "        .email-content {" +
                        "            padding: 20px;" +
                        "            text-align: center;" +
                        "        }" +
                        "        .otp-code {" +
                        "            font-size: 24px;" +
                        "            color: #0073e6;" +
                        "            font-weight: bold;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .email-footer {" +
                        "            margin-top: 20px;" +
                        "            text-align: center;" +
                        "            font-size: 14px;" +
                        "            color: #666;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"email-container\">" +
                        "        <div class=\"email-header\">" +
                        "            <h1>Noize Health Care</h1>" +
                        "        </div>" +
                        "        <div class=\"email-content\">" +
                        "            <h2>OTP Verification</h2>" +
                        "            <p>Dear User,</p>" +
                        "            <p>To continue with your secure access at Noize Health Care, please use the OTP below:</p>" +
                        "            <div class=\"otp-code\">" + otp + "</div>" +
                        "            <p>This OTP is valid for 10 minutes. For your security, please do not share it with anyone.</p>" +
                        "            <p>If you did not request this, please ignore this email.</p>" +
                        "            <p>Thank you for using Noize Health Care.</p>" +
                        "        </div>" +
                        "        <div class=\"email-footer\">" +
                        "            <p>Noize Health Care</p>" +
                        "            <p>&copy; 2024 Noize Health Care Team. All rights reserved.</p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>")
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email sent successfully with ID: " + data.getId());
        } catch (ResendException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public static void sendQr(String email, String qrImageUrl) throws ResendException, IOException {
        Resend resend = new Resend(System.getenv("RESEND_API_KEY"));

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Login QR Code</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 4px 8px rgba(0,0,0,0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #4CAF50;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.5;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .qr-code {\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .qr-code img {\n" +
                "            max-width: 200px;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "        .btn-container {\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 10px 20px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #45a049;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Login via QR Code</h1>\n" +
                "        <p>Hello,</p>\n" +
                "        <p>You can use the QR code below to log into your account securely. Simply scan the QR code with your authentication app or mobile device to proceed.</p>\n" +
                "        <div class=\"qr-code\">\n" +
                "            <img src=\""+ qrImageUrl +"\" alt=\"Login QR Code\">" +
                "        </div>\n" +
                "        <p>If you're unable to scan the QR code, you can log in manually by clicking the button below:</p>\n" +
                "        <div class=\"btn-container\">\n" +
                "            <a href=\"https://yourloginlink.com\" class=\"btn\">Login Here</a>\n" +
                "        </div>\n" +
                "        <p>If you did not request this login, please ignore this email or contact our support team.</p>\n" +
                "        <p>Thank you,<br>Your Company Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        Attachment att = Attachment.builder()
                .path(qrImageUrl)
                .fileName("UserQR.png")
                .build();

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Noize Health Care <noizehealthcare@punylk.me>")
                .to(email)
                .subject("Login QR Code")
                .attachments(att)
                .html(html)
                .build();

        CreateEmailResponse data = resend.emails().send(params);
        System.out.println(data.getId());
        //QrController.deleteQrCode(qrFilePath);
    }
}
