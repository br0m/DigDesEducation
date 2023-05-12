package com.digdes.java2023.dto.member;

public class MemberDto {

    private long id;
    private String displayedName;
    private String jobTitle;
    private String account;
    private String email;
    private String status;

    public void setId(long id) {
        this.id = id;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
