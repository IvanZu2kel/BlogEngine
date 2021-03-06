package com.example.blogengine.repository;

import com.example.blogengine.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {
    @Query(value = "SELECT c.code FROM captcha_codes c WHERE c.secret_code = :secret_captcha",
            nativeQuery = true)
    String checkCaptcha(@Param("secret_captcha") String secret_captcha);

    @Query("select c from CaptchaCodes c where c.secretCode = :secret")
    CaptchaCodes findBySecretCode(String secret);

    @Query("select c from CaptchaCodes c order by c.id desc ")
    List<CaptchaCodes> findLastUpdating();
}
