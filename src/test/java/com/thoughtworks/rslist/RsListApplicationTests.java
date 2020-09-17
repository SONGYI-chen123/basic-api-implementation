package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RsListApplicationTests {
    @Autowired
    MockMvc mockmvc;

    @Test
    @Order(1)
    public void should_get_rs_event() throws Exception {
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$",hasSize(3))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(jsonPath("$[2].eventName",is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord",is("无标签"))).
                andExpect(jsonPath("$[2].user",not(hasKey("user")))).
                andExpect(status().isOk());

    }

    @Test
    @Order(2)
    public void should_get_one_rs_event() throws Exception {
        mockmvc.perform(get("/rs/1")).
                andExpect(jsonPath("$.eventName",is("第一条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(jsonPath("$.user",not(hasKey("user")))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/2")).
                andExpect(jsonPath("$.eventName",is("第二条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(jsonPath("$.user",not(hasKey("user")))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/3")).
                andExpect(jsonPath("$.eventName",is("第三条事件"))).
                andExpect(jsonPath("$.keyWord",is("无标签"))).
                andExpect(jsonPath("$.user",not(hasKey("user")))).
                andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_get_rs_event_between()throws Exception{
        mockmvc.perform(get("/rs/list?start=1&end=2")).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(status().isOk());
       mockmvc.perform(get("/rs/list?start=2&end=3")).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[0].keyWord", is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[1].keyWord", is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(status().isOk());
        mockmvc.perform(get("/rs/list?start=1&end=3")).
                andExpect(jsonPath("$",hasSize(3))).
                andExpect(jsonPath("$[0].eventName",is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord",is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName",is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord",is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(jsonPath("$[2].eventName",is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord",is("无标签"))).
                andExpect(jsonPath("$[2].user",not(hasKey("user")))).
                andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void should_add_rs_event() throws Exception{

        User user = new User("yichen","female",18,"1577660501@163.com","15179945008");

        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockmvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$", hasSize(4))).
                andExpect(jsonPath("$[0].eventName", is("第一条事件"))).
                andExpect(jsonPath("$[0].keyWord", is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord", is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(jsonPath("$[2].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord", is("无标签"))).
                andExpect(jsonPath("$[2].user",not(hasKey("user")))).
                andExpect(jsonPath("$[3].eventName", is("猪肉涨价了"))).
                andExpect(jsonPath("$[3].keyWord", is("经济"))).
                andExpect(jsonPath("$[3].user",not(hasKey("user")))).
                andExpect(status().isOk());

    }

    @Test
    @Order(5)
    public void  should_modify_rs_event() throws Exception{
        String MjsonString =  "{\"eventName\":\"高考出成绩了\",\"keyWord\":\"民生\"}";
        mockmvc.perform(patch("/rs/Mevent/1").content(MjsonString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$", hasSize(4))).
                andExpect(jsonPath("$[0].eventName", is("高考出成绩了"))).
                andExpect(jsonPath("$[0].keyWord", is("民生"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[1].keyWord", is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(jsonPath("$[2].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[2].keyWord", is("无标签"))).
                andExpect(jsonPath("$[2].user",not(hasKey("user")))).
                andExpect(jsonPath("$[3].eventName", is("猪肉涨价了"))).
                andExpect(jsonPath("$[3].keyWord", is("经济"))).
                andExpect(jsonPath("$[3].user",not(hasKey("user")))).
                andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void  should_delete_rs_event() throws Exception{
        mockmvc.perform(delete("/rs/1")).
                andExpect(status().isCreated());
        mockmvc.perform(get("/rs/list")).
                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$[0].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[0].keyWord", is("无标签"))).
                andExpect(jsonPath("$[0].user",not(hasKey("user")))).
                andExpect(jsonPath("$[1].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[1].keyWord", is("无标签"))).
                andExpect(jsonPath("$[1].user",not(hasKey("user")))).
                andExpect(jsonPath("$[2].eventName", is("猪肉涨价了"))).
                andExpect(jsonPath("$[2].keyWord", is("经济"))).
                andExpect(jsonPath("$[2].user",not(hasKey("user")))).
                andExpect(status().isOk());
    }


}
