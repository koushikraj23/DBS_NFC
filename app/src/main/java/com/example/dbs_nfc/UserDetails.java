package com.example.dbs_nfc;

public class UserDetails {

    private String name;
    private String cardId;
    private String pswd;

    public UserDetails() {
    }


    public UserDetails(String name, String cardId, String pswd) {
        this.name = name;
        this.cardId = cardId;
        this.pswd = pswd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
