package com.example.training.training_project.entity;

import java.util.Date;
import java.util.UUID;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.EventInput;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
@TableName(value = "tb_event")
public class EventEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Float price;
    private Date date;
    private Integer creatorId;

    public static EventEntity fromEventEntity(EventInput input){
        EventEntity newEvent = new EventEntity();
        newEvent.setTitle(input.getTitle());
        newEvent.setDescription(input.getDescription());
        newEvent.setPrice(input.getPrice());
        newEvent.setDate(DateUtil.convertISOStringToDate(input.getDate()));
        newEvent.setCreatorId(input.getCreatorId());
        return newEvent;
    }
}
