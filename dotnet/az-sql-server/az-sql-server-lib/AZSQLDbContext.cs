using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;

namespace az_sql_server
{
    internal class AZSQLDbContext : DbContext
    {
        private readonly string dbConnString;

        public AZSQLDbContext(string dbConnString) {
            this.dbConnString = dbConnString;
            this.Database.EnsureCreated();
        }

        internal DbSet<TaskExecutionInformation> TaskExecutionInformation { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {            
            optionsBuilder.UseSqlServer(dbConnString);                
        }
    }
}