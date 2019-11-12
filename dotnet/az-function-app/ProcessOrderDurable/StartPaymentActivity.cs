using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using com.opusmagus.bl;

namespace com.opusmagus.api
{
    [FunctionName("StartPaymentActivity")]
    public class StartPaymentActivity
    {
        public static string Run(string name)
        {
            System.Threading.Thread.Sleep(12000);
            return $"Starting payment activity for order number {name}!";
        }
    }
}
