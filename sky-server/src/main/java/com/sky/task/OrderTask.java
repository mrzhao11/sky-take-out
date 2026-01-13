package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，用于处理订单相关的定时任务
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的定时任务方法
     * 该方法会被定时触发，用于处理那些超时未支付的订单
     */
    @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    public void processTimeooutOrder(){
        log.info("处理超时订单的定时任务执行了", LocalDateTime.now());

        // 获取当前时间15分钟前的时间点
        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrdertimeLT(Orders.PENDING_PAYMENT, orderTime);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("超时未支付，系统自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
                log.info("订单已取消，订单号：{}", orders.getNumber());
            }
        } else {
            log.info("没有超时未支付的订单需要处理");
        }

    }


    /**
     * 处理已送达订单的定时任务方法
     * 该方法会被定时触发，用于处理那些已送达的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveredOrder(){
        log.info("处理已送达订单的定时任务执行了", LocalDateTime.now());

        // 获取当前时间60分钟前的时间点
        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(-60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrdertimeLT(Orders.DELIVERY_IN_PROGRESS, orderTime);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
                log.info("订单已完成，订单号：{}", orders.getNumber());
            }
        } else {
            log.info("没有已送达的订单需要处理");
        }

    }

}
