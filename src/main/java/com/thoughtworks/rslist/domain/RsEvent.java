package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;

@Data
public class RsEvent {
    private String eventName;
    private String keyWord;
    int voteNum;
    int rsEventId;
    @Valid
    private int userId;

    public RsEvent(String eventName, String keyWord,int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;

    }
    public RsEvent(String eventName,String keyWord,int rsEventId,int voteNum){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.rsEventId = rsEventId;
        this.voteNum = voteNum;
    }



}
