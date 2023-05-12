
package com.digdes.java2023.dto.project;

public class ChangeProjectDto {

    private String codename;
    private String newCodename;
    private String newTitle;
    private String newDescription;

    public String getNewCodename() {
        return newCodename;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setNewCodename(String newCodename) {
        this.newCodename = newCodename;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

}
