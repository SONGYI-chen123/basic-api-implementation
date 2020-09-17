package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import com.thoughtworks.rslist.api.UserController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  UserController userController = new UserController();
  private List<RsEvent> rsList = initRsEventList();

  private List<RsEvent>initRsEventList(){
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("yichen","female",18,"1577660501@163.com","15178945858");
    rsEventList.add(new RsEvent("第一条事件","无标签",user));
    rsEventList.add(new RsEvent("第二条事件","无标签",user));
    rsEventList.add(new RsEvent("第三条事件","无标签",user));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index-1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent){
    for(int i=0;i<userController.userList.size();i++){
      if(!(userController.userList.get(i).getName().equals(rsEvent.getUser().getName()))){
        userController.userList.add(rsEvent.getUser());
      }
    }
        rsList.add(rsEvent);
      return ResponseEntity.created(null).build();

  }

  @PatchMapping("/rs/Mevent/{index}")
  public ResponseEntity modifyRsEvent(@RequestBody RsEvent mrsEvent ,@PathVariable int index){
    rsList.set(index-1,mrsEvent);
    return ResponseEntity.created(null).build();
  }
  @DeleteMapping("/rs/{index}")
  public  ResponseEntity deleteRsEvent(@PathVariable int index){
    rsList.remove(index-1);
    return ResponseEntity.created(null).build();
  }


}