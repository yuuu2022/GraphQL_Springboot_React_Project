package com.example.training.training_project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.training_project.entity.EventEntity;

@Mapper
public interface EventEntityMapper extends BaseMapper<EventEntity>{
    
}
