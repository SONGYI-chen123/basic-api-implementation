package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    @PostMapping("/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote){
        Optional<UserPo> userPo = userRepository.findById(vote.getUserId());
        if(vote.getVoteNum()<= userPo.get().getVoteNum()){
            int voteNum = userPo.get().getVoteNum()-vote.getVoteNum();
            userPo.get().setVoteNum(voteNum);
            Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
            int RsVoteNum = vote.getVoteNum() + rsEventPo.get().getVoteNum();
            rsEventPo.get().setVoteNum(RsVoteNum);
            VotePo votePo = VotePo.builder().voteNum(vote.getVoteNum()).voteTime(vote.getVoteTime()).userPo(userPo.get()).rsEventPo(rsEventPo.get()).build();
            voteRepository.save(votePo);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }



}
