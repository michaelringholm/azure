package dk.commentor.bl.command;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

import dk.commentor.dtl.OrderDTO;
import dk.commentor.sl.IOrderService;

public class GetOrdersCommand implements ICommand<String, CompletableFuture<List<OrderDTO>>>
{
    private final IOrderService orderService;
    private final Logger logger;

    public GetOrdersCommand(IOrderService orderService, Logger logger) throws Exception
    {
        if (orderService == null) throw new Exception("orderService is null");
        if (logger == null) throw new Exception("logger is null");
        this.orderService = orderService;
        this.logger = logger;
    }

    public CompletableFuture<List<OrderDTO>> Execute(String input) throws Exception
    {
        logger.debug("Executing command GetOrdersCommand...");
        List<OrderDTO> orders = orderService.GetOrders().get();
        logger.debug("Command GetOrdersCommand executed!");
        return CompletableFuture.completedFuture(orders);
    }
}