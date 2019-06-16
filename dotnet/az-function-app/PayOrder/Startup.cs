using System;
using com.opusmagus.bl;
using com.opusmagus.logging;
using Microsoft.Azure.Functions.Extensions.DependencyInjection;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

[assembly: FunctionsStartup(typeof(com.opusmagus.api.Startup))]
namespace com.opusmagus.api
{
    public class Startup : FunctionsStartup
    {
        public override void Configure(IFunctionsHostBuilder builder)
        {
            builder.Services.AddSingleton<ILogger, OpusMagusLogger>();
            builder.Services.AddSingleton<ILoggerProvider, OpusMagusLoggerProvider>();
            builder.Services.AddSingleton<ProcessOrderCommand, ProcessOrderCommand>();
        }
    }
}