package com.example.dbs_nfc;

public class UserDetails {

    private String name;

    public String getDbsID() {
        return dbsID;
    }

    public void setDbsID(String dbsID) {
        this.dbsID = dbsID;
    }

    private String dbsID;
    private String cardId;
    private String pswd;
    private String fine;
    private String book;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;
    public UserDetails() {
    }


    public UserDetails(String name, String cardId, String pswd,String uuid) {
        this.name = name;
        this.cardId = cardId;
        this.pswd = pswd;
        this.uuid=uuid;
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
