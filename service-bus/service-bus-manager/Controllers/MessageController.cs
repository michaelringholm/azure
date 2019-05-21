using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.ServiceBus;

[Controller]
public class MessageController {

    [Route("/api/sb")]
    public QueueClient GetServiceBusNamespaces() {
        //new SubscriptionClient();
        //Microsoft.Azure.Management.F
        var serviceBusConnectionString = "Endpoint=sb://cc-dev-sbu.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=RS7wB9Uh01Zwo3s2XSeR/Qc8l97w0gU2GmQpG1woMko=";
        var sbConn = new ServiceBusConnection(serviceBusConnectionString, TimeSpan.MaxValue);
        const int numberOfMessages = 10;
        var queueName = "cc-dev-q1";
        var queueClient = new QueueClient(serviceBusConnectionString, queueName);
        return queueClient;
    }
}