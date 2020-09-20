package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VoteControllerTest {
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    MockMvc mockMvc;

    UserPo userPo;
    RsEventPo rsEventPo;

    @BeforeEach
    void setup() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
        userPo = UserPo.builder().name("xiaoyi").gender("male").age(19).email("123@qq.com").phone("10000000000").voteNum(10).build();
        userRepository.save(userPo);
        rsEventPo = RsEventPo.builder().eventName("事件1").keyWord("无标签").userPo(userPo).voteNum(0).build();
        rsEventRepository.save(rsEventPo);

    }

    @Test
    @Order(2)
    public void should_vote_success_when_voteNum_less_than_user() throws Exception{
        UserPo userPo = UserPo.builder().name("xiaoyi").gender("male").age(19).email("123@qq.com").phone("10000000000").voteNum(10).build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("事件1").keyWord("无标签").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        int rsEventId = rsEventPo.getId();
        Vote vote = new Vote(5, LocalDateTime.now(),userPo.getId(),rsEventId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/rsEventId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(3)
    public void should_vote_success_when_voteNum_more_than_user() throws Exception{
        UserPo userPo = UserPo.builder().name("xiaoer").gender("male").age(19).email("123@qq.com").phone("10000000001").voteNum(10).build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("事件2").keyWord("无标签").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        int rsEventId = rsEventPo.getId();
        Vote vote = new Vote(13, LocalDateTime.now(),userPo.getId(),rsEventId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/rsEventId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(4)
    public void should_get_VoteRecord()throws  Exception{
        for(int i =0;i<8;i++){
            VotePo votePo = VotePo.builder().userPo(userPo).rsEventPo(rsEventPo).voteTime(LocalDateTime.now()).voteNum(i+1).build();
            voteRepository.save(votePo);
        }


        mockMvc.perform(get("/voteRecord").param("userId",String.valueOf(userPo.getId()))
                .param("rsEventId",String.valueOf(rsEventPo.getId()))
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(1)))
                .andExpect(jsonPath("$[1].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[1].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[1].voteNum",is(2)))
                .andExpect(jsonPath("$[2].voteNum",is(3)))
                .andExpect(jsonPath("$[3].voteNum",is(4)))
                .andExpect(jsonPath("$[4].voteNum",is(5)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/voteRecord").param("userId",String.valueOf(userPo.getId()))
                .param("rsEventId",String.valueOf(rsEventPo.getId()))
                .param("pageIndex","2"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(6)))
                .andExpect(jsonPath("$[1].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[1].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[1].voteNum",is(7)))
                .andExpect(jsonPath("$[2].voteNum",is(8)))
                .andExpect(status().isOk());

    }

    @Test
    @Order(1)
    public void should_get_VoteRecord_from_startVoteTime_to_endVoteTime() throws Exception {
        VotePo votePo = VotePo.builder().userPo(userPo).voteTime(LocalDateTime.now()).rsEventPo(rsEventPo).voteNum(5).build();
        voteRepository.save(votePo);
        mockMvc.perform(get("/findByTime")
                .param("startVoteTime", "2020-09-20 00:00:00")
                .param("endVoteTime","2020-09-22 00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(votePo.getVoteNum())));

    }
}
