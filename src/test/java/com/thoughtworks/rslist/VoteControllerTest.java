package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setup(){
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    public void should_vote_success_when_voteNum_more_than_user() throws Exception{
        UserPo userPo = UserPo.builder().name("xiaoyi").gender("male").age(19).email("123@qq.com").phone("10000000000").voteNum(10).build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("事件1").keyWord("无标签").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        int rsEventId = rsEventPo.getId();
        Vote vote = new Vote(5, LocalDateTime.now(),userPo.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/rsEventId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
