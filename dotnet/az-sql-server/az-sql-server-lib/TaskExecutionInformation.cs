using System;

namespace az_sql_server
{
    public class TaskExecutionInformation
    {
        public Guid Id { get; set; }
        public string Status { get; set; }
        public int Duration { get; set; }
    }
}