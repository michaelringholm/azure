package org.openerp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;

import dk.commentor.dtl.OrderDTO;
import dk.commentor.sl.IOrderService;

public class OpenERPService implements IOrderService {
    private Logger logger;

    public OpenERPService(Logger logger) {
        this.logger = logger;
    }

    public CompletableFuture<OrderDTO> GetOrder(String shoppingCardId) {
        logger.info("OpenERP getting order...");
        return CompletableFuture.supplyAsync(() -> {
            try {            
                //var random = Math.Random();            
                OrderDTO order = new OrderDTO();
                order.ID = UUID.randomUUID();
                order.OrderID=1;
                order.OrderDate = Calendar.getInstance();//.add(Calendar.DAY_OF_MONTH, (int)(Math.random()*29));
                return order;
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }        
        });
    }

    public CompletableFuture<List<OrderDTO>> GetOrders() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<OrderDTO> orders = new ArrayList<OrderDTO>();
                //var random = Math.Random();
                for (int i = 0; i < 20; i++) {
                    OrderDTO order = new OrderDTO();
                    order.ID = UUID.randomUUID();
                    order.OrderID=1+i;
                    order.OrderDate = Calendar.getInstance();//.add(Calendar.DAY_OF_MONTH, (int)(Math.random()*29));
                    orders.add(order);
                }
                return orders;
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });            
    }

    public CompletableFuture<Void> ShipOrder(OrderDTO order) {
        logger.info("OpenERP sipping order...");
        return CompletableFuture.runAsync(() -> {
            try {
                return;
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });    
    }

    public CompletableFuture<Boolean> ValidateOrder(OrderDTO order) {
        logger.info("OpenERP validating order...");
        return CompletableFuture.supplyAsync(() -> {
            try {
                return true;
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });    
    }
}
