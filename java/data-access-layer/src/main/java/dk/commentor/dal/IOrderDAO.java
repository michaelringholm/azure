package dk.commentor.dal;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dtl.OrderDTO;

public interface IOrderDAO {
    CompletableFuture<Void> Store(OrderDTO order);
    CompletableFuture<OrderDTO> Restore(String orderNo);
    CompletableFuture<List<OrderDTO>> Restore();
}
