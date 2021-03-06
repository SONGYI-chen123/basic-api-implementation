package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;


import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.*;
import com.thoughtworks.rslist.api.UserController;

import javax.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;



  @GetMapping("/rs/{id}")
  public ResponseEntity getOneRsEvent(@PathVariable int id){

    if(id<=0){
      throw new RsEventNotValidException("invalid index");
    }else {
      return ResponseEntity.ok(rsEventRepository.findById(id));
    }
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam  (required = false)Integer start, @RequestParam(required = false)Integer end){
    List<RsEventPo> rsEventPos = rsEventRepository.findAll();
    List<RsEvent> rsEvents = new ArrayList<>();
    if(start == null || end == null){
      for(int i=0;i<rsEventPos.size();i++) {
        rsEvents.add(new RsEvent(rsEventPos.get(i).getEventName(),rsEventPos.get(i).getKeyWord(),rsEventPos.get(i).getId(),rsEventPos.get(i).getVoteNum()));
      }
      return ResponseEntity.ok(rsEvents);
    }
    if(start<=0|| end > rsEventPos.size()){
      throw new RsEventNotValidException("invalid request param");
    }
    for(int i=start-1;i<end;i++){
      rsEvents.add(new RsEvent(rsEventPos.get(i).getEventName(),rsEventPos.get(i).getKeyWord(),rsEventPos.get(i).getId(),rsEventPos.get(i).getVoteNum()));
    }
    return ResponseEntity.ok(rsEvents);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) {
    Optional<UserPo> userPo = userRepository.findById(rsEvent.getUserId());
    if(!userPo.isPresent()){
      return ResponseEntity.badRequest().build();
    }else{
     RsEventPo rsEventPo = RsEventPo.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName()).userPo(userPo.get()).build();
     rsEventRepository.save(rsEventPo);
     return ResponseEntity.created(null).build();}

  }

  @PatchMapping("rs/{rsEventId}")
  public ResponseEntity patchRsEvent(@RequestBody RsEvent rsEvent,@PathVariable int rsEventId){
     Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
      if(rsEventPo.get().getUserPo().getId() == rsEvent.getUserId()){
        if(!rsEvent.getKeyWord().equals("null")){
            rsEventPo.get().setKeyWord(rsEvent.getKeyWord());}
        if (!rsEvent.getEventName().equals("null")){
            rsEventPo.get().setEventName(rsEvent.getEventName());}
      return ResponseEntity.ok(rsEventPo);
    }else {
        return ResponseEntity.badRequest().build();
      }
  }




}