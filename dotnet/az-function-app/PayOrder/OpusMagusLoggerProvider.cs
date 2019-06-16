using Microsoft.Extensions.Logging;

namespace com.opusmagus.logging
{
    public class OpusMagusLoggerProvider : ILoggerProvider
    {
        public ILogger CreateLogger(string categoryName)
        {
            return new OpusMagusLogger();
        }

        public void Dispose()
        {
            throw new System.NotImplementedException();
        }
    }
}