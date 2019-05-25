using Microsoft.Extensions.Logging;

namespace com.opusmagus.bl
{
    public class ProcessOrderCommand : ICommand
    {
        private ILogger logger;

        public ProcessOrderCommand(ILogger logger) {
            this.logger = logger;
        }
        public dynamic Execute(dynamic input)
        {
            logger.LogInformation("Processing order...");
            return new {};
        }
    }
}