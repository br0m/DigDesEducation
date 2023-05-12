package com.digdes.java2023.dto.member;

import com.digdes.java2023.dto.enums.MemberStatus;

public class ChangeMemberDto extends OperationByIdMemberDto {

    private String newFirstName;
    private String newLastName;
    private String newPatronymic;
    private String newJobTitle;
    private String newAccount;
    private String newEmail;
    private MemberStatus newStatus;

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getNewPatronymic() { return newPatronymic; }

    public void setNewPatronymic(String newPatronymic) { this.newPatronymic = newPatronymic; }

    public String getNewJobTitle() {
        return newJobTitle;
    }

    public void setNewJobTitle(String newJobTitle) {
        this.newJobTitle = newJobTitle;
    }

    public String getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(String newAccount) {
        this.newAccount = newAccount;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public MemberStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(MemberStatus newStatus) {
        this.newStatus = newStatus;
    }
}
