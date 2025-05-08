package controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {

    // Twilio credentials (use env variables in production)
    private static final String TWILIO_SID = System.getenv("TWILIO_SID");
    private static final String TWILIO_AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String TWILIO_PHONE = System.getenv("TWILIO_PHONE");

    // Static initializer to set up Twilio once
    static {
        Twilio.init(TWILIO_SID, TWILIO_AUTH_TOKEN);
    }

    // Public method to send an SMS
    public static void sendSms(String to, String messageText) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),             // To
                    new PhoneNumber(TWILIO_PHONE),   // From
                    messageText                      // Message body
            ).create();

            System.out.println("✅ SMS sent! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("❌ Failed to send SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
