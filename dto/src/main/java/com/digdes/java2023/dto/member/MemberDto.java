package com.digdes.java2023.dto.member;

public class MemberDto {

    private long id;
    private String displayedName;
    private String jobTitle;
    private String account;
    private String email;
    private String status;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDisplayedName() {
        return displayedName;
    }
    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
