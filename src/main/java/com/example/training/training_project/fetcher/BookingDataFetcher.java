package com.example.training.training_project.fetcher;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.training.training_project.custom.AuthContext;
import com.example.training.training_project.entity.BookingEntity;
import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.mapper.BookingEntityMapper;
import com.example.training.training_project.mapper.EventEntityMapper;
import com.example.training.training_project.mapper.UserEntityMapper;
import com.example.training.training_project.type.Booking;
import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.User;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DgsComponent
public class BookingDataFetcher{
    private final BookingEntityMapper bookingEntityMapper;
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public BookingDataFetcher(BookingEntityMapper bookingEntityMapper, EventEntityMapper eventEntityMapper, UserEntityMapper userEntityMapper){
        this.bookingEntityMapper = bookingEntityMapper;
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }

    @DgsQuery
    public List<Booking> bookings(DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        UserEntity user = authContext.getUserEntity();
        List<BookingEntity> bookingEntities = bookingEntityMapper.selectList(new QueryWrapper<BookingEntity>().eq("user_id", user.getId()));
        return bookingEntities.stream().map(Booking::fromEntity).collect(Collectors.toList());
    }

    @DgsMutation
    public Booking bookEvent(@InputArgument String eventId, DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        UserEntity userEntity = authContext.getUserEntity();

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserId(userEntity.getId());
        bookingEntity.setEventId(Integer.parseInt(eventId));
        bookingEntity.setCreateAt(new Date());
        bookingEntity.setUpdateAt(new Date());

        bookingEntityMapper.insert(bookingEntity);
        Booking booking = Booking.fromEntity(bookingEntity);

        return booking;
   }

   @DgsMutation
   public Event cancelBooking(@InputArgument String bookingId, DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        UserEntity userEntity = authContext.getUserEntity();

        BookingEntity bookingEntity = bookingEntityMapper.selectById(Long.parseLong(bookingId));
        if(bookingEntity == null){
            throw new RuntimeException("Booking not found");
        }

        if(!bookingEntity.getUserId().equals(userEntity.getId())){
            throw new RuntimeException("User not authorized to cancel booking");
        }

        bookingEntityMapper.deleteById(Long.parseLong(bookingId));
        EventEntity eventEntity = eventEntityMapper.selectById(bookingEntity.getEventId());
        return Event.fromEntity(eventEntity);
   }

   @DgsData(parentType = "Booking", field = "user")
   public User user(DataFetchingEnvironment dfe){
       Booking booking = dfe.getSource();
       UserEntity userEntity = userEntityMapper.selectById(booking.getUserId());
       return User.fromEntity(userEntity);
   }

    @DgsData(parentType = "Booking", field = "event")
    public Event event(DataFetchingEnvironment dfe){
        Booking booking = dfe.getSource();
        EventEntity eventEntity = eventEntityMapper.selectById(booking.getEventId());
        Event event = Event.fromEntity(eventEntity);
        return event;
    }
}