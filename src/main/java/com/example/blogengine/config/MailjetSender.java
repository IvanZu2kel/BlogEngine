package com.example.blogengine.config;

import com.example.blogengine.config.properties.MailjetRegistrationParam;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailjetSender {
    private final MailjetRegistrationParam mailjet;

    public void send(String emailTo, String message) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(ClientOptions.builder().apiKey(mailjet.getKey()).apiSecretKey(mailjet.getSecret()).build());
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", mailjet.getFrom())
                                        .put("Name", "DevPub"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", emailTo)
                                                .put("Name", "DevPub")))
                                .put(Emailv31.Message.SUBJECT, "Ссылка на восстановалние пароля")
                                .put(Emailv31.Message.TEXTPART, "")
                                .put(Emailv31.Message.HTMLPART, message)
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        log.info(String.valueOf(response.getStatus()));
        log.info(String.valueOf(response.getData()));
    }
}

