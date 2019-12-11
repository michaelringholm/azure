package dk.commentor.bl.command;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

import dk.commentor.bl.model.ProcessOrderInputModel;
import dk.commentor.dtl.OrderDTO;
import dk.commentor.sl.IOrderService;
import dk.commentor.sl.IPaymentService;

public class ProcessOrderCommand implements ICommand<ProcessOrderInputModel, CompletableFuture<OrderDTO>>
{
    private final IOrderService orderService;
    private final IPaymentService paymentService;
    private final Logger logger;

    public ProcessOrderCommand(IOrderService orderService, IPaymentService paymentService, Logger logger) throws Exception
    {
        if (orderService == null) throw new Exception("orderService is null");
        if (paymentService == null) throw new Exception("paymentService is null");
        if (logger == null) throw new Exception("logger is null");
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.logger = logger;
    }

    public CompletableFuture<OrderDTO> Execute(ProcessOrderInputModel input) throws Exception
    {
        logger.debug("Executing command ProcessOrderCommand...");
        OrderDTO order = orderService.GetOrder(input.ShoppingCartId).get();
        boolean isValid = orderService.ValidateOrder(order).get();
        logger.debug(String.format("isValid=%s", isValid));
        if (isValid)
        {
            boolean paymentCompleted = paymentService.ProcessPayment(order).get();
            if (paymentCompleted)
                orderService.ShipOrder(order).get();
            else
                throw new Exception("Payment failed!");
        }
        else
            throw new Exception("Order is invalid!");
        logger.debug("Command ProcessOrderCommand executed!");

        return CompletableFuture.completedFuture(order);
    }
}
