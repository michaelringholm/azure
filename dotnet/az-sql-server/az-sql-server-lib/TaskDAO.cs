using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace az_sql_server
{
    public class TaskDAO : ITaskDAO
    {
        private readonly string dbConnString;
        private readonly AZSQLDbContext context;

        private TaskDAO() {
            
        }
        public TaskDAO(string dbConnString) {
            this.dbConnString = dbConnString;
            context = new AZSQLDbContext(dbConnString);
        }

        public async Task<IEnumerable<TaskExecutionInformation>> GetExecutionInformation()
        {
            return await context.TaskExecutionInformation.ToListAsync();
        }

        public async Task SaveExecutionInformation(TaskExecutionInformation executionInfo) {            
            context.Add(executionInfo);
            await context.SaveChangesAsync();
        }
    }
}
