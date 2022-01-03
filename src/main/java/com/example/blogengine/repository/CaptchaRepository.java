package com.example.blogengine.repository;

import com.example.blogengine.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {

    @Modifying
    @Query(value = "INSERT INTO captcha_codes (code, secret_code, time) VALUES (:code, :secret_code, :time)",
            nativeQuery = true)
    void insertCaptcha(@Param("code") String code, @Param("secret_code") String secret_code, @Param("time") Date time);

    @Query(
            value = "SELECT c.code FROM captcha_codes c WHERE c.secret_code = :secret_captcha",
            nativeQuery = true)
    String checkCaptcha(@Param("secret_captcha") String secret_captcha);

    @Query("select c from CaptchaCodes c where c.secretCode = :secret")
    CaptchaCodes findBySecretCode(String secret);
}
