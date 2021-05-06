package com.neu.reliidentyBack.dao;

import com.neu.reliidentyBack.domain.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jasonR
 * @date 2021/4/30 19:12
 */
@Mapper
@Repository
public interface TicketMapper {
    Ticket selectTicketByContent(String ticket);
    int insertLoginTicket(Ticket ticket);
    int insertUseTicket(Ticket ticket);
    int updateUseTime(int time,int id);
    int updateStatus(String ticket);
}
