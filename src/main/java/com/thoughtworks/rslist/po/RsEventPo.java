package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "rsEvent")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventPo {
   @Id
   @GeneratedValue
    int id;
    String eventName;
    String keyWord;
    @ManyToOne
    UserPo userPo;

    public UserPo getUserPo() {
        return userPo;
    }

    public void setUserPo(UserPo userPo) {
        this.userPo = userPo;
    }
}
