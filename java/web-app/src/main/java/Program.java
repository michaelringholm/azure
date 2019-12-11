import System;
import System.Collections.Generic;
import System.IO;
import System.Linq;
import System.Threading.Tasks;
import dk.commentor.logger;
import Microsoft.AspNetCore;
import Microsoft.AspNetCore.Hosting;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.Logging;

package web_app
{
    public class Program
    {
        internal static CommentorLoggerProvider LoggerProvider;

        public static void Main(String[] args)
        {
            LoggerProvider = new CommentorLoggerProvider();
            CreateWebHostBuilder(args).Build().Run();
        }

        public static IWebHostBuilder CreateWebHostBuilder(String[] args) =>
            WebHost.CreateDefaultBuilder(args)
                .UseStartup<Startup>()
                .ConfigureLogging(logging => logging.AddProvider(LoggerProvider));
    }
}
