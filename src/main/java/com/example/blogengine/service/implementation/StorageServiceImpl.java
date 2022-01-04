package com.example.blogengine.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.blogengine.exception.IncorrectFormatException;
import com.example.blogengine.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class StorageServiceImpl implements StorageService {
    private final int LENGTH = 16;
    private final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    private final Cloudinary cloudinary;

    public StorageServiceImpl() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "db5r7hl7p",
                "api_key", "285628396215742",
                "api_secret", "eJLaV6tCOwJ77r-V0Re03gXoIbk"));
    }

    public String store(MultipartFile image) throws IOException, IncorrectFormatException {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = (int) (SYMBOLS.length() * Math.random());
            sb.append(SYMBOLS.charAt(index));
        }
        if (!Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf('.')).equals(".png")
                && !image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')).equals(".jpg")) {
            throw new IncorrectFormatException();
        }
        if (image.getSize() > 5242880) {
            throw new MaxUploadSizeExceededException(5242880);
        }
        String UPLOAD = "upload/";
        String path = UPLOAD + sb.substring(0, 4) + "/" + sb.substring(4, 8) + "/" + sb.substring(8, 12);
        path = path + "/" + sb.substring(12, 16);

        return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                "public_id", path)).get("url").toString();
    }

    public String storeAvatar(MultipartFile image) throws IncorrectFormatException, IOException {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = (int) (SYMBOLS.length() * Math.random());
            sb.append(SYMBOLS.charAt(index));
        }
        String AVATARS = "avatars/";
        String path = AVATARS + sb.substring(0, 4) + "/" + sb.substring(4, 8) + "/" + sb.substring(8, 12);
        Files.createDirectories(Path.of(path));
        path = path + "/" + sb.substring(12, 16);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cropImage(image.getInputStream()), "jpg", baos);
        return cloudinary.uploader().upload(baos.toByteArray(), ObjectUtils.asMap(
                "public_id", path)).get("url").toString();
    }

    private BufferedImage cropImage(InputStream inputStream) throws IOException {
        BufferedImage imBuff = ImageIO.read(inputStream);
        ImageFilter filter = new CropImageFilter(0, 0, 36, 36);
        Image cropped = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imBuff.getSource(), filter));
        BufferedImage image = new BufferedImage(36, 36, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.drawImage(cropped, 0, 0, null);
        g.dispose();
        return image;
    }

}