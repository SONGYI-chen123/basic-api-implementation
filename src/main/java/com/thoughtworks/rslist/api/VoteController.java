package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId,@RequestParam int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex-1,5);
        return ResponseEntity.ok(
                voteRepository.findAccordingToUserAndRsEvent(userId,rsEventId,pageable).stream().map(
                        item -> Vote.builder().userId(item.getUserPo().getId())
                        .voteTime(item.getVoteTime())
                        .rsEventId(item.getRsEventPo().getId())
                        .voteNum(item.getVoteNum()).build()
                ).collect(Collectors.toList()));

    }

    @GetMapping("/findByTime")
    public ResponseEntity<List<Vote>> findVoteByStartTimeAndEndTime(@RequestParam String startVoteTime, @RequestParam String endVoteTime) {
        LocalDateTime startTimeLocal =ChangeStringToLcalTimeDate(startVoteTime);
        LocalDateTime endTimeLocal = ChangeStringToLcalTimeDate(endVoteTime);
        List<VotePo> votePOs = voteRepository.findAllFromStartTimeToEndTime(startTimeLocal,endTimeLocal);
        List<Vote> votes = votePOs.stream()
                .map(
                item -> Vote.builder().userId(item.getUserPo().getId())
                        .voteTime(item.getVoteTime())
                        .rsEventId(item.getRsEventPo().getId())
                        .voteNum(item.getVoteNum()).build()
                 ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);


    }

    private LocalDateTime ChangeStringToLcalTimeDate(String VoteTime) {
        DateTimeFormatter dataTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime voteTime = LocalDateTime.parse(VoteTime,dataTime);
        return voteTime;
    }


}
