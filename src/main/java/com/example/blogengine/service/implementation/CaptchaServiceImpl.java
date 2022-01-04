package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.security.AuthCaptchaResponse;
import com.example.blogengine.model.CaptchaCodes;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.service.CaptchaService;
import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaRepository captchaRepository;

    public AuthCaptchaResponse getCaptcha() throws IOException {
        final Cage cage = new GCage();
        String token = cage.getTokenGenerator().next();
        String secretKey = generateSecretKey();
        String base64String = ConvertImageToBase64(token, cage);

        AuthCaptchaResponse authCaptchaResponse = new AuthCaptchaResponse()
                .setSecret(secretKey)
                .setImage(base64String);
        CaptchaCodes captchaCodes = new CaptchaCodes()
                .setCode(token)
                .setSecretCode(secretKey)
                .setTime(new Date());
        captchaRepository.save(captchaCodes);
        return authCaptchaResponse;
    }

    private String ConvertImageToBase64(String token, Cage cage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ImResizer(cage.drawImage(token)), "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageInByte);
    }

    private BufferedImage ImResizer(BufferedImage image) {
        int newWidth = 100;
        int newHeight = 35;
        BufferedImage newImage = new BufferedImage(
                newWidth, newHeight, BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return newImage;
    }

    private String generateSecretKey() {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
