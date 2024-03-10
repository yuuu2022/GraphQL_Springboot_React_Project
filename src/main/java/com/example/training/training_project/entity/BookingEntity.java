package com.example.training.training_project.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tb_booking")
public class BookingEntity {
    private Integer id;
    private Integer eventId;
    private Integer userId;
    private Date createAt;
    private Date updateAt;
}