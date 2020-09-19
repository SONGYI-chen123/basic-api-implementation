package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

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
    public void should_register_user() throws Exception{
        User user = new User("yichen","female",18,"1577660501@163.com","15178945858");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserPo> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("yichen",all.get(0).getName());
        assertEquals("female",all.get(0).getGender());

    }

    @Test
    @Order(2)
    public void should_get_user_by_Id() throws Exception{
        mockMvc.perform(get("/user/1")).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_delete_user_by_Id() throws Exception{
        mockMvc.perform(delete("/user/1")).andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    public void name_should_less_than_8() throws Exception{
        User user = new User("yichen1234","female",18,"1577660501@163.com","15178945858");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void age_should_between_18_and_100() throws Exception{
        User user = new User("yichen","female",15,"1577660501@163.com","15178945858");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    public void email_should_suit_format() throws Exception{
        User user = new User("yichen","female",18,"1577660501","15178945858");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    public void phone_should_suit_format() throws Exception{
        User user = new User("yichen","female",18,"1577660501@163.com","151789458");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}