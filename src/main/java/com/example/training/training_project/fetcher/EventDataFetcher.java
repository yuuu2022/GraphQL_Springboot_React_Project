package com.example.training.training_project.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.mapper.EventEntityMapper;
import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.EventInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
@Slf4j
@DgsComponent
public class EventDataFetcher {
    private final EventEntityMapper eventEntityMapper;

    public EventDataFetcher(EventEntityMapper eventEntityMapper){
        this.eventEntityMapper = eventEntityMapper;
    }
    
    @DgsQuery
    public List<Event> events(){
        List<EventEntity> eventEntities = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> events = eventEntities.stream()
                            .map(Event::fromEntity).collect(Collectors.toList());
        return events;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name ="eventInput") EventInput input){
        EventEntity eventEntity = EventEntity.fromEventEntity(input);
        eventEntityMapper.insert(eventEntity);
        return Event.fromEntity(eventEntity);
    }
}
