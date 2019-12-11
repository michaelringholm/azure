package dk.commentor.sl;

import java.util.concurrent.CompletableFuture;

import dk.commentor.dtl.OrderDTO;

public interface IPaymentService
{
    CompletableFuture<Boolean> ProcessPayment(OrderDTO order);
}
