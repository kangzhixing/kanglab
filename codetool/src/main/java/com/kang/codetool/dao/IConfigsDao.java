package com.kang.codetool.dao;

import com.kang.codetool.model.Configs;

import java.util.List;

public interface IConfigsDao{

    Configs selectById(long id);

    List<Configs> selectAll();

    List<Configs> selectByPage(Configs configs);

    List<Configs> selectByWhere(Configs configs);

    int count(Configs configs);

    int deleteById(long id);

    int updateById(Configs configs);

    int updateByIdSelective(Configs configs);

    int insert(Configs configs);

    int insertSelective(Configs configs);

}