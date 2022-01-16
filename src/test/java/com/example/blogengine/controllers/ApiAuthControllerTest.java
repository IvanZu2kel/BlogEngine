package com.example.blogengine.controllers;

import com.example.blogengine.AbstractTest;
import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.request.PasswordRequest;
import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.model.CaptchaCodes;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiAuthControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    private CaptchaCodes captchaCodes;

    @BeforeEach
    public void setup() {
        super.setup();
        captchaCodes = new CaptchaCodes()
                .setCode("1234")
                .setSecretCode("4321")
                .setTime(new Date());
        captchaRepository.save(captchaCodes);
    }

    @AfterEach
    public void clear() {
        captchaRepository.delete(captchaCodes);
    }

    @Test
    void getCheckWithoutPrincipal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void getCheckWithPrincipal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(userRepository.findByEmail("test@test.ru").get().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.moderationCount")
                        .value(0));
    }

    @Test
    @WithMockUser(username = "test5@test.ru", authorities = "user:write")
    void getCheckWithPrincipalException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .principal(() -> "test5@test.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test3@test.ru", authorities = "user:moderate")
    void getCheckWithUserModer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(userRepository.findByEmail("test3@test.ru").get().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.moderationCount")
                        .value(postRepository.findPostByModerationStatus().size()));
    }

    @Test
    void getCaptcha() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/captcha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.secret")
                        .value(captchaRepository.findLastUpdating().get(0).getSecretCode()));
    }

    @Test
    void postRegister() throws Exception {
        UserRequest userRequest = new UserRequest()
                .setEMail("testov1@test.ru")
                .setName("Testov")
                .setPassword("Testov321")
                .setCaptcha("1234")
                .setCaptchaSecret("4321");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();
        userRepository.delete(userRepository.findByEmail("testov1@test.ru").get());

        userRequest.setPassword("123");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password")
                        .value("Пароль короче 6-ти символов")).andReturn();

        userRequest.setPassword("123456");
        userRequest.setEMail("test@test.ru");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email")
                        .value("Этот e-mail уже зарегистрирован")).andReturn();

        userRequest.setPassword("123456")
                .setEMail("testov1@test.ru")
                .setCaptcha("1222");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.captcha")
                        .value("Код с картинки введён неверно")).andReturn();

        userRequest.setPassword("123456")
                .setEMail("testov1@test.ru")
                .setCaptcha("1234")
                .setName("Ivan Abramov");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name")
                        .value("Имя указано неверно. Имя может содержать буквы латинского алфавита, цифры или знак подчеркивания"));
    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test2@test.ru");
        loginRequest.setPassword("test2@test.ru");
        User user = userRepository.findByEmail("test2@test.ru").orElseThrow();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.moderation").value(false)).andReturn();

        loginRequest.setEmail("123231234@mail.re");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false)).andReturn();
    }

    @Test
    void postPassword() throws Exception {
        User user = userRepository.findByEmail("test@test.ru").orElseThrow();
        user.setCode("12345678");
        userRepository.save(user);

        PasswordRequest passwordRequest = new PasswordRequest()
                .setCode("12345678")
                .setPassword("43211234")
                .setCaptcha("1234")
                .setCaptchaSecret("4321");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

        passwordRequest.setPassword("1234");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false));
    }
}
