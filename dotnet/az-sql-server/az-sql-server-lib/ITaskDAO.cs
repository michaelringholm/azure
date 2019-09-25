using System.Collections.Generic;
using System.Threading.Tasks;

namespace az_sql_server
{
    public interface ITaskDAO
    {
        Task SaveExecutionInformation(TaskExecutionInformation executionInfo);
        Task<IEnumerable<TaskExecutionInformation>> GetExecutionInformation();
    }
}