package com.voting.userauth.payload;

public class VerifyRequest {
    private String jwtToken;

    public VerifyRequest(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public VerifyRequest() {
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

