# Create the function app

# Create your first function

# DI
Install the following nuget package.
Microsoft.Azure.Functions.Extensions

Add a startup class.

``` csharp
[assembly:FunctionsStartup(typeof(dk.commentor.starterproject.api.Startup))]
namespace dk.commentor.starterproject.api
{
    
    public class Startup : FunctionsStartup
    {
        public override void Configure(IFunctionsHostBuilder builder)
        {
            builder.Services.AddSingleton<ILogger, CommentorLogger>();
            Console.WriteLine("Function started!");
        }
    }
}
```

Change the class containing your function to non-static and add a constructor for accepting the services required.

``` csharp
namespace dk.commentor.starterproject.api
{
    public class ProcessOrder
    {
        private ProcessOrderCommand processOrderCommand;

        public ProcessOrder(ProcessOrderCommand processOrderCommand) {
            this.processOrderCommand = processOrderCommand;
        }

        [FunctionName("ProcessOrder")]
        public async Task<IActionResult> Run([HttpTrigger(AuthorizationLevel.Function, "post", Route = null)] HttpRequest req, ILogger log)
        {
        }
    }
}
```


# Good links
https://docs.microsoft.com/en-us/sandbox/functions-recipes/cli
https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference-csharp
https://thedatafarm.com/serverless/building-c-project-based-azure-functions-in-visual-studio-code/