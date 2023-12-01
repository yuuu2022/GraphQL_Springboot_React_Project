package com.example.training.training_project.fetcher;

import java.util.Arrays;
import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class EventDataFetcher {

    @DgsQuery
    public List<String> events(){
        return Arrays.asList("this","is","a","list");
    }

    @DgsMutation
    public String createEvenet(@InputArgument String name){
        return name + "Created"; 
    }
}
