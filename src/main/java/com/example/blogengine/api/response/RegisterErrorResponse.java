package com.example.blogengine.api.response;

public class RegisterErrorResponse {

    private String email;
    private String name;
    private String password;
    private String captcha;

    public void setEmail() {
        this.email = "Этот e-mail уже зарегистрирован";
    }

    public void setName() {
        this.name = "Имя указано неверно, Имя может содержать буквы латинского алфавита, цифры или знак подчеркивания";
    }

    public void setPassword() {
        this.password = "Пароль короче 6-ти символов";
    }

    public void setCaptcha() {
        this.captcha = "Код с картинки введён неверно";
    }
}
