package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
    User user = new User("yichen","female",18,"1577660501@163.com","151789458");
    rsEventList.add(new RsEvent("第一条事件","无标签",user));
    rsEventList.add(new RsEvent("第二条事件","无标签",user));
    rsEventList.add(new RsEvent("第三条事件","无标签",user));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start == null || end == null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent){
    for(int i=0;i<userController.userList.size();i++){
      if(!(userController.userList.get(i).getName().equals(rsEvent.getUser().getName()))){
        userController.userList.add(rsEvent.getUser());
      }
    }
        rsList.add(rsEvent);

  }

  @PatchMapping("/rs/Mevent/{index}")
  public void modifyRsEvent(@RequestBody RsEvent mrsEvent ,@PathVariable int index){
    rsList.set(index-1,mrsEvent);
  }
  @DeleteMapping("/rs/{index}")
  public  void deleteRsEvent(@PathVariable int index){
    rsList.remove(index-1);
  }


}