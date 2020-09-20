package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    int voteNum;
    @ManyToOne
    UserPo userPo;
    @OneToMany(cascade = CascadeType.REMOVE , mappedBy = "rsEvent")
    private List<VotePo> votePos;


}
