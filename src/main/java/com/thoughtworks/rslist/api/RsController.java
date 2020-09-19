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

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

  private static void createTableByJdbc() throws SQLException {

    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsSystem","root","123456");

    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet resultSet = metaData.getTables(null,null,"rsEvent",null);
    if(!resultSet.next()){
      String createTableSql = "create table reEvent(eventName varchar(200) not null,keyWord varchar(100) not null)";
      Statement statement = connection.createStatement();
      statement.execute(createTableSql);
    }
    connection.close();
  }


  @GetMapping("/rs/{id}")
  public ResponseEntity getOneRsEvent(@PathVariable int id){

    return ResponseEntity.ok(rsEventRepository.findById(id));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam  (required = false)Integer start, @RequestParam(required = false)Integer end){
    List<RsEventPo> rsEventPos = rsEventRepository.findAll();
    if(start == null || end == null){
      return ResponseEntity.ok(rsEventPos);
    }
    if(start<=0|| end > rsEventPos.size()){
      throw new RsEventNotValidException(("invalid request param"));
    }
    return ResponseEntity.ok(rsEventPos.subList(start-1,end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) {
    if(!userRepository.findById(rsEvent.getUserId()).isPresent()){
      return ResponseEntity.badRequest().build();
    }else{
     RsEventPo rsEventPo = RsEventPo.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName()).userId(rsEvent.getUserId()).build();
     rsEventRepository.save(rsEventPo);
      return ResponseEntity.created(null).build();}

  }




}