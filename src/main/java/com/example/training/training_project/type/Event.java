package com.example.training.training_project.type;

import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
public class Event {
    private String id;
    private String title;
    private String description;
    private Float price;
    private String date;   

    public static Event fromEntity(EventEntity eventEntity){
        Event event = new Event();
        event.setId(eventEntity.getId().toString());
        event.setTitle(eventEntity.getTitle());
        event.setDescription(eventEntity.getDescription());
        event.setPrice(eventEntity.getPrice());
        event.setDate(DateUtil.formatDateInISOString(eventEntity.getDate()));
        return event;
    }
}
