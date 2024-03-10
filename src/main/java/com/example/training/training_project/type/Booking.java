package com.example.training.training_project.type;
import com.example.training.training_project.entity.BookingEntity;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
public class Booking{
    private Integer id;
    private User user;
    private Integer userId;
    private Event event;
    private Integer eventId;
    private String updateAt;
    private String createAt;
    
    public static Booking fromEntity(BookingEntity entity){
        Booking booking = new Booking();
        booking.setId(entity.getId());
        booking.setUserId(entity.getUserId());
        booking.setEventId(entity.getEventId());
        booking.setCreateAt(DateUtil.formatDateInISOString(entity.getCreateAt()));
        booking.setUpdateAt(DateUtil.formatDateInISOString(entity.getUpdateAt()));
        return booking;
    }
}