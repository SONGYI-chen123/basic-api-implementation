package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.RsEventRepository;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;

import java.awt.*;
import java.util.*;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RsListApplicationTests {
    @Autowired
    MockMvc mockmvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        UserPo userPo = UserPo.builder().name("xiaoyi").gender("male").age(19).email("123@qq.com").phone("10000000000").voteNum(10).build();
        userRepository.save(userPo);
        UserPo userPo1 = UserPo.builder().name("xiaoer").gender("male").age(20).email("123@qq.com").phone("10000000001").voteNum(10).build();
        userRepository.save(userPo1);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("事件1").keyWord("无标签").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEventPo rsEventPo1 = RsEventPo.builder().eventName("事件2").keyWord("无标签").userPo(userPo1).build();
        rsEventRepository.save(rsEventPo1);
    }

    @Test
    @Order(1)
    public void should_get_rs_event() throws Exception {
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].eventName",is("事件1"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].eventName",is("事件2"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(status().isOk());

    }

    @Test
    @Order(2)
    public void should_get_one_rs_event() throws Exception {
        mockmvc.perform(get("/rs/1")).
                andExpect(jsonPath("$.eventName",is("事件1"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/2")).
                andExpect(jsonPath("$.eventName",is("事件2"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_get_rs_event_between()throws Exception{
        mockmvc.perform(get("/rs/list?start=1&end=2")).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].eventName",is("事件1"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].eventName",is("事件2"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(status().isOk());

    }

    @Test
    @Order(4)
    public void should_add_rs_event_when_user_not_Exist() throws Exception{
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":188}";

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());


    }

    @Test
    @Order(5)
    public void should_add_rs_event_when_user_Exist() throws Exception{
        UserPo saveUser = userRepository.save(UserPo.builder().name("xiaosan").age(20).gender("male").email("123@qq.com").phone("18888888888").voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":"+saveUser.getId()+"}";

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(saveUser,all.get(0).getUserPo());

    }


    @Test
    @Order(6)
    public void should_throw_rs_event_not_vaild_exception() throws Exception{
        mockmvc.perform(get("/rs/0")).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    @Order(7)
    public void should_throw_rs_event_start_or_end_not_vaild_exception() throws Exception{
        mockmvc.perform(get("/rs/list?start=0&end=2")).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid request param")));
    }

    @Test
    @Order(8)
    public void should_throw_method_argument_not_valid_exception() throws Exception{
        UserPo saveUser = userRepository.save(UserPo.builder().name("xiaoyi").age(20).gender("male").email("123@qq.com").phone("18888888888").voteNum(10).build());
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",saveUser.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid param")));
    }

    @Test
    @Order(9)
    public void should_patch_rs_Event_when_id_match_userId() throws Exception{
        UserPo userPo = UserPo.builder().name("xiaowu").gender("female").age(24).email("123@qq.com").phone("10000000004").voteNum(10).build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("事件3").keyWord("无标签").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEvent rsEvent = new RsEvent("高考出成绩了","生活",userPo.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        int rsEventId = rsEventPo.getId();
        mockmvc.perform(patch("/rs/rsEventId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
