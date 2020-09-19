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
    }

    @Test
    @Order(1)
    public void should_get_rs_event() throws Exception {
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$",hasSize(3))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(jsonPath("$[2].eventName",is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord",is("无标签"))).
                andExpect(status().isOk());

    }

    @Test
    @Order(2)
    public void should_get_one_rs_event() throws Exception {
        mockmvc.perform(get("/rs/1")).
                andExpect(jsonPath("$.eventName",is("第一条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/2")).
                andExpect(jsonPath("$.eventName",is("第二条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/3")).
                andExpect(jsonPath("$.eventName",is("第三条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_get_rs_event_between()throws Exception{
        mockmvc.perform(get("/rs/list?start=1&end=2")).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(status().isOk());
       mockmvc.perform(get("/rs/list?start=2&end=3")).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[0].keyWord", is("无标签"))).
                andExpect(jsonPath("$[1].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[1].keyWord", is("无标签"))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/list?start=1&end=3")).
                andExpect(jsonPath("$",hasSize(3))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(jsonPath("$[2].eventName",is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord",is("无标签"))).
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
        UserPo saveUser = userRepository.save(UserPo.builder().name("xiaoyi").age(20).gender("male").email("123@qq.com").phone("18888888888").voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":"+saveUser.getId()+"}";

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(saveUser.getId(),all.get(0).getUserId());

    }


    @Test
    public void should_throw_rs_event_not_vaild_exception() throws Exception{
        mockmvc.perform(get("/rs/0")).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    public void should_throw_rs_event_start_or_end_not_vaild_exception() throws Exception{
        mockmvc.perform(get("/rs/list?start=0&end=2")).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid request param")));
    }

    @Test
    public void should_throw_method_argument_not_valid_exception() throws Exception{
        UserPo saveUser = userRepository.save(UserPo.builder().name("xiaoyi").age(20).gender("male").email("123@qq.com").phone("18888888888").voteNum(10).build());
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",saveUser.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error",is("invalid param")));
    }
}
