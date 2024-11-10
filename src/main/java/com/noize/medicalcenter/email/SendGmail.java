package com.noize.medicalcenter.email;

import com.noize.medicalcenter.notification.AlertNotification;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class SendGmail {
    public static void sendEmail(String email, String otp) {
        String apiKey = System.getenv("RESEND_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API key is missing. Please set RESEND_API_KEY in environment variables.");
            return;
        }

        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("noizehealthcare@sldatabase.ninja")
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
}
