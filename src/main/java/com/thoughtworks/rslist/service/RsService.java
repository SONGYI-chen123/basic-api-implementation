package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Configuration
public class RsService {
    UserRepository userRepository;
    RsEventRepository rsEventRepository;
    VoteRepository voteRepository;
    @Bean
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Bean
    public void setRsEventRepository(RsEventRepository rsEventRepository){
        this.rsEventRepository = rsEventRepository;
    }


    @Bean
    public void setVoteRepository(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }



}