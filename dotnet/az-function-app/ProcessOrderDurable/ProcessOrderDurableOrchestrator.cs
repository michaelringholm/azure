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
    public class ProcessOrderDurableOrchestrator
    {
        public ProcessOrderDurableOrchestrator()
        {
        }

        public static async Task<List<string>> Run(DurableOrchestrationContext context)
        {
            var input = context.GetInput<SampleInput>();
            var outputs = new List<string>();

            context.SetCustomStatus("Starting payment activity");
            outputs.Add(await context.CallActivityAsync<string>("start-payment-activity", "A"));
            if (input.OrderAmount == null) throw new Exception("OrderAmount was missing!");
            if (input.OrderAmount > 100000)
            {
                context.SetCustomStatus("Waiting for manager approval");
                await context.WaitForExternalEvent("ManagerApproval");
            }
            context.SetCustomStatus("Starting shipping activity");
            outputs.Add(await context.CallActivityAsync<string>("start-shipping-activity", "B"));
            context.SetCustomStatus("Starting evaluation activity");
            outputs.Add(await context.CallActivityAsync<string>("start-evaluation-activity", "C"));
            context.SetCustomStatus("All activities are done");
            return outputs;
        }

        internal class SampleInput
        {
            public decimal OrderAmount { get; set; }
        }
    }
}
