﻿using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos.Table;
//using Microsoft.Azure.Storage;
using Microsoft.Extensions.Configuration;

namespace storage_account_tables
{
    class Program
    {
        private static string _storageConnectionString;
        private static IConfiguration configuration;

        public string StorageConnectionString { get; set; }
        static void Main(string[] args)
        {
            Console.WriteLine("Started");
            LoadAppSettings();
            //var tableName = "invoice";
            IInvoiceDAO invoiceDAO = new InvoiceDAO(configuration); // Use DI            
            InsertSampleData(invoiceDAO);
            var sampleData = RetrieveSampleDate(invoiceDAO);
            invoiceDAO.Drop();
            Console.WriteLine("Ended!");
        }

        private static object RetrieveSampleDate(IInvoiceDAO invoiceDAO)
        {
            return invoiceDAO.GetAll();
        }

        private static void InsertSampleData(IInvoiceDAO invoiceDAO)
        {
            invoiceDAO.InsertOrMerge(new InvoiceDTO{ InvoiceID = Guid.NewGuid(), InvoiceNumber = 100200, InvoiceYear = 2019, InvoiceMonth = 10 });
        }

        public static void LoadAppSettings()
        {
            var env = "dev"; //Environment.GetEnvironmentVariables("");
            configuration = new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile($"appsettings.{env}.json").Build();            
        }       
    }
}
