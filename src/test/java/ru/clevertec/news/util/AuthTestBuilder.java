package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.constant.RoleName;

@Builder(setterPrefix = "with")
public class AuthTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String login = "Admin111";

    @Builder.Default
    private String password = "Admin";

    @Builder.Default
    private RoleName role = RoleName.ADMIN;

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public JwtDto buildJwtDto() {
        return new JwtDto("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBZG1pbiIsInVzZXJuYW1lIjoiQWRtaW4iLCJyb2xlIjoiQURNSU4iLCJleHAiOjE3MDkxNjY1NjZ9.26SzqC817uuNXtk6TsGp6ga0HW_GM9HlGBAsoXW9tr0");
    }
}