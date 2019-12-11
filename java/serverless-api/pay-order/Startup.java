import System;
import com.paypal;
import dk.commentor.bl.command;
import dk.commentor.logger;
import dk.commentor.sl;
import Microsoft.Azure.Functions.Extensions.DependencyInjection;
import Microsoft.Azure.WebJobs;
import Microsoft.Azure.WebJobs.Hosting;
import Microsoft.Extensions.DependencyInjection;
import Microsoft.Extensions.Logging;
import org.openerp;

[assembly:FunctionsStartup(typeof(dk.commentor.starterproject.api.Startup))]
package dk.commentor.starterproject.api
{
    
    public class Startup implements FunctionsStartup
    {
        public override void Configure(IFunctionsHostBuilder builder)
        {
            builder.Services.AddSingleton<Logger, CommentorLogger>();
            builder.Services.AddSingleton<IPaymentService, PayPalService>();
            builder.Services.AddSingleton<IOrderService, OpenERPService>();
            builder.Services.AddSingleton<ProcessOrderCommand>();
            Console.WriteLine("Function started!");
        }
    }
}
