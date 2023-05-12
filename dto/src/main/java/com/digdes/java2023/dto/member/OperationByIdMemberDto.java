package com.digdes.java2023.dto.member;

//Используется для изменения статуса и получения карточки сотрудника по ID
public class OperationByIdMemberDto {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
