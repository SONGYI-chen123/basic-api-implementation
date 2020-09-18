package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    public static List<User> userList = new ArrayList<>();




    @PostMapping("/user")
    public ResponseEntity assUser(@RequestBody @Valid User user){
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
    public ResponseEntity getUserList(@PathVariable int id){
        Optional<UserPo> userPo = userRepository.findById(id);

        return ResponseEntity.ok(userPo);
    }
}
