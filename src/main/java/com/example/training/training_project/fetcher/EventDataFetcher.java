package com.example.training.training_project.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.mapper.EventEntityMapper;
import com.example.training.training_project.mapper.UserEntityMapper;
import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.EventInput;
import com.example.training.training_project.type.User;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsFederationResolver;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class EventDataFetcher {
    //@Autowired
    private final EventEntityMapper eventEntityMapper;
    //@Autowired
    private final UserEntityMapper userEntityMapper;

    // public EventDataFetcher(EventEntityMapper eventEntityMapper){
    //     this.eventEntityMapper = eventEntityMapper;
    // }
    
    @DgsQuery
    public List<Event> events(){
        List<EventEntity> eventEntities = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> events = eventEntities.stream()
                            .map(Event::fromEntity).collect(Collectors.toList());
        // List<Event> events = eventEntities.stream()
        // .map(eventEntity->{
        //     Event event = Event.fromEntity(eventEntity);
        //     populateEventWithUser(event, eventEntity.getCreatorId());
        //     //event.setCreator(getCreatorById(eventEntity.getCreatorId()));
        //     return event;
        // }).collect(Collectors.toList());
        return events;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name ="eventInput") EventInput input){
        EventEntity newEventEntity = EventEntity.fromEventEntity(input);
        eventEntityMapper.insert(newEventEntity);
        Event newEvent = Event.fromEntity(newEventEntity);
        //populateEventWithUser(newEvent, newEventEntity.getCreatorId());

        //Integer id  = newEventEntity.getCreatorId();
        //newEvent.setCreator(getCreatorById(newEventEntity.getCreatorId()));
        return newEvent;
    }

    private void populateEventWithUser(Event event, Integer userId){
            UserEntity userEntity = userEntityMapper.selectById(userId);
            User user = User.fromEntity(userEntity);
            event.setCreator(user);
    }

    private User getCreatorById(Integer userId){
        UserEntity userEntity = userEntityMapper.selectById(userId);
        User user = User.fromEntity(userEntity);
        return user;
    }

    @DgsData(parentType ="Event" , field = "creator")
    public User creator(DgsDataFetchingEnvironment dfe){
        Event event = dfe.getSource();
        UserEntity userEntity = userEntityMapper.selectById(event.getCreatorId());
        User user = User.fromEntity(userEntity);
        return user;
    }
}
