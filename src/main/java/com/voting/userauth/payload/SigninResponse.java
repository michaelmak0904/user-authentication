package com.voting.userauth.payload;

/**
 * @author michaelmak
 */
public class SigninResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SigninResponse(String token) {
        this.token = token;
    }

    private String token;
}
