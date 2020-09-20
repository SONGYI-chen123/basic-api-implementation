package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPo;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RsEventRepository extends CrudRepository<RsEventPo,Integer> {
    List<RsEventPo> findAll();
    @Transactional
    void deleteAllByUserId(int userId);
}
