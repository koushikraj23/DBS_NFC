package com.example.dbs_nfc;

public class BookDetails {
    private String title;
    private String author;
    private String publications;
    private String dueDate;
    private String rLink;

    public BookDetails() {
    }

    public BookDetails(String title, String author,  String dueDate, String rLink) {
        this.title = title;
        this.author = author;

        this.dueDate = dueDate;
        this.rLink = rLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublications() {
        return publications;
    }

    public void setPublications(String publications) {
        this.publications = publications;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getrLink() {
        return rLink;
    }

    public void setrLink(String rLink) {
        this.rLink = rLink;
    }
}
