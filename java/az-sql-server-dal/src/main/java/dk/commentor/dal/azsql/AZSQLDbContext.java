package dk.commentor.dal.azsql;

class AZSQLDbContext implements DbContext {
    private final String dbConnString;
    DbSet<OrderDTO>Orders;

    public AZSQLDbContext(String dbConnString) {
        this.dbConnString = dbConnString;
        this.Database.EnsureCreated();
    }

    protected void OnConfiguring(DbContextOptionsBuilder optionsBuilder) {
        optionsBuilder.UseSqlServer(dbConnString);
    }
}
