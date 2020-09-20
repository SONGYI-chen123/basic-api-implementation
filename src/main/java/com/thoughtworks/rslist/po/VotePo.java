package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePo {
    @Id
    @GeneratedValue()
    private int id;
    private int voteNum;
    private LocalDateTime voteTime;
    @ManyToOne
    private RsEventPo rsEventPo;

    @ManyToOne
    @JoinColumn
    private UserPo userPo;

}
