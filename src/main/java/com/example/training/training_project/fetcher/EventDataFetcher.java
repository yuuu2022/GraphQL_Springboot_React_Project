package com.example.training.training_project.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.EventInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class EventDataFetcher {
    private List<Event> events = new ArrayList<>();
    
    @DgsQuery
    public List<String> events(){
        return Arrays.asList("this","is","a","list");
    }

    @DgsMutation
    public Event createEvenet(@InputArgument(name ="eventInput") EventInput input){
         Event newEvent = new Event();
         newEvent.setId(UUID.randomUUID().toString());
         newEvent.setTitle(input.getTitle());
         newEvent.setDescription(input.getDescription());
         newEvent.setPrice(input.getPrice());
         newEvent.setDate(input.getDate());

         events.add(newEvent);
         return newEvent;
    }
}
