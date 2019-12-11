package dk.commentor.dal.azsql;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dal.IOrderDAO;
import dk.commentor.dtl.OrderDTO;
import dk.commentor.sl.ISecurityVault;

public class AZSQLOrderDAO implements IOrderDAO {
    private final AZSQLDbContext context;

    public AZSQLOrderDAO(ISecurityVault securityVault) {
        String dbConnString = securityVault.GetSecret("az-sql-conn");
        context = new AZSQLDbContext(dbConnString);
    }

    public CompletableFuture<List<OrderDTO>> Restore() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return context.Orders.ToListAsync();
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });                
    }

    public CompletableFuture<OrderDTO> Restore(String orderNo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return context.Orders.ToListAsync().SingleOrDefault();
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });              
    }

    public CompletableFuture Store(OrderDTO order) {
        return CompletableFuture.runAsync(() -> {
            try {
                context.Add(order);
                context.SaveChangesAsync(); // await
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });      
        
    }
}
