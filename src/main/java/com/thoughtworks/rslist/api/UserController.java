package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public List<User> userList = initUserList();

    private List<User> initUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("yichen","female",18,"1577660501@163.com","15179945008"));
        userList.add(new User("xiaoyi","male",22,"1577661@163.com","15178900000"));
        return userList;
    }


    @PostMapping("/user")
    public ResponseEntity assUser(@RequestBody @Valid User user){
        userList.add(user);
        return ResponseEntity.created(null)
                .header("index",Integer.toString(userList.size())).build();
    }

    @GetMapping("/user")
    public List<User> getUserList(){
        return userList;
    }
}
