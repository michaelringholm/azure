package dk.commentor.sl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dtl.OrderDTO;

public interface IOrderService {
	CompletableFuture<Boolean> ValidateOrder(OrderDTO order);
	CompletableFuture<Void> ShipOrder(OrderDTO order);
	CompletableFuture<OrderDTO> GetOrder(String shoppingCardId);
	CompletableFuture<List<OrderDTO>> GetOrders();
}
