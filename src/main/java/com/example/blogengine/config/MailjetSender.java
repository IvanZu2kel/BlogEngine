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
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
public class MailjetSender {
    private final MailjetRegistrationParam mailjet;
    private final String recovery;

    private String htmlTextRecovery;

    public MailjetSender(MailjetRegistrationParam mailjet,
                         @Value(value = "${html.file.recovery}")String recovery) {
        this.mailjet = mailjet;
        this.recovery = recovery;
    }

    public void send(String emailTo, String message) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        String messageResponse;
        String[] split = message.split("/");
        if (Arrays.asList(split).contains("login")) {
            messageResponse = htmlTextRecovery.replace("$message", message);
        } else messageResponse = message;
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
                                .put(Emailv31.Message.HTMLPART, messageResponse)
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        log.info(String.valueOf(response.getStatus()));
        log.info(String.valueOf(response.getData()));
    }

    @PostConstruct
    private void getFileByPathRegister() {
        File htmlMessageFileRecovery = new File(recovery);
        try {
            htmlTextRecovery = FileUtils.readFileToString(htmlMessageFileRecovery);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

