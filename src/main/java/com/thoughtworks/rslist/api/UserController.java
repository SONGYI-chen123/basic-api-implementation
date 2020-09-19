package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketRequesterAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    public static List<User> userList = new ArrayList<>();


    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user){
        UserPo userPo = new UserPo();
        userPo.setName(user.getName());
        userPo.setGender(user.getGender());
        userPo.setAge(user.getAge());
        userPo.setEmail(user.getEmail());
        userPo.setPhone(user.getPhone());
        userPo.setVoteNum(user.getVoteNum());
        userRepository.save(userPo);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable int id){
        Optional<UserPo> userPo = userRepository.findById(id);

        return ResponseEntity.ok(userPo);
    }

    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.created(null).build();
    }


}
