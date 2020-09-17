package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
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
    if(index<=0 || index > rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam  (required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    if(start<=0|| end > rsList.size()){
      throw new RsEventNotValidException(("invalid request param"));
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent){
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