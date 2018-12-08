using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net.Http.Headers;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;

namespace com.opusmagus.cloud.blobs
{
    class Program
    {
        private static string blobContainerConnString = null;
        private static string blobContainerName = null;
        static void Main(string[] args)
        {
            Console.WriteLine("Azure blob store sample started...");
            //blobContainerConnString = ConfigurationManager.AppSettings["StorageConnectionString"].ToString();
            blobContainerConnString = "DefaultEndpointsProtocol=https;AccountName=cc0v20dev0sa;AccountKey=9pv60XZrKDieQFHnSWh/r8aEzIHLfKElLT/lT4DoHvvpBcnhYIpXP57M/1euvUKfF9DyWgZ5j7E2ac7DMkGt7Q==";
            blobContainerName = "sample-blob-container";
            //demo1();
            //demo2();
            //demo3();
            demo4();
            Console.WriteLine("Azure blob store sample ended!");
        }

        private static void demo4()
        {
            IBlobService blobService = new AzureBlobService();
            var blobItems = blobService.getBlobItems(blobContainerConnString, blobContainerName, 10);
            var blobItemEnumerator = blobItems.GetEnumerator();
            blobItemEnumerator.MoveNext();
            var blobName = ((CloudBlockBlob)blobItemEnumerator.Current).Name;
            var blobContents = blobService.getBlobContents(blobContainerConnString, blobContainerName, blobName);
            File.WriteAllBytes($"./local/{Guid.NewGuid()}.docx", blobContents);
        }

        private static void demo1()
        {
            // Retrieve storage account information from connection string
            // How to create a storage connection string - http://msdn.microsoft.com/en-us/library/azure/ee758697.aspx
            //ConfigurationManager.AppSettings["StorageConnectionString"].ToString()
            string blobContainerName = "sample-blob-container";
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(blobContainerConnString);

            // Create a blob client for interacting with the blob service.
            var blobClient = storageAccount.CreateCloudBlobClient();
            var blobContainer = blobClient.GetContainerReference(blobContainerName);
            blobContainer.CreateIfNotExistsAsync().ConfigureAwait(false).GetAwaiter().GetResult();

            // To view the uploaded blob in a browser, you have two options. The first option is to use a Shared Access Signature (SAS) token to delegate  
            // access to the resource. See the documentation links at the top for more information on SAS. The second approach is to set permissions  
            // to allow public access to blobs in this container. Comment the line below to not use this approach and to use SAS. Then you can view the image  
            // using: https://[InsertYourStorageAccountNameHere].blob.core.windows.net/webappstoragedotnet-imagecontainer/FileName 
            blobContainer.SetPermissionsAsync(new BlobContainerPermissions { PublicAccess = BlobContainerPublicAccessType.Blob });
            // Gets all Cloud Block Blobs in the blobContainerName and passes them to teh view
            List<Uri> allBlobs = new List<Uri>();
            /*foreach (IListBlobItem blob in blobContainer.ListBlobs())
            {
                if (blob.GetType() == typeof(CloudBlockBlob))
                    allBlobs.Add(blob.Uri);
            }*/
        }

        private static void demo2()
        {
            string blobContainerName = "sample-blob-container";
            var storageAccount = CloudStorageAccount.Parse(blobContainerConnString);
            var blobClient = storageAccount.CreateCloudBlobClient();
            var container = blobClient.GetContainerReference(blobContainerName);
            //var file = picture.File;
            //var parsedContentDisposition = ContentDispositionHeaderValue.Parse(file.ContentDisposition);
            //var filename = Path.Combine(parsedContentDisposition.FileName.Trim('"'));
            //var blockBlob = container.GetBlockBlobReference(filename);
            //blockBlob.UploadFromStreamAsync(file.OpenReadStream()).ConfigureAwait(false).GetAwaiter().GetResult();
        }

        private static void demo3()
        {
            var urls = new List<string>();            
            string blobContainerName = "sample-blob-container";
            var storageAccount = CloudStorageAccount.Parse(blobContainerConnString);
            var blobClient = storageAccount.CreateCloudBlobClient();
            var container = blobClient.GetContainerReference(blobContainerName);
            var blobItems = container.ListBlobsSegmentedAsync("", true, BlobListingDetails.All, 200, null, null, null).ConfigureAwait(false).GetAwaiter().GetResult().Results;
            foreach(var blobItem in blobItems) {
                Debug.WriteLine($"blobUri:{blobItem.Uri}");
                var blockBlob = (CloudBlockBlob)blobItem;
                blockBlob.DownloadToFileAsync($"./local/{Guid.NewGuid()}.docx", FileMode.CreateNew).ConfigureAwait(false).GetAwaiter().GetResult();
            }
            //var blobRef = container.GetBlobReference(blobItem.Uri);
            
        }

    }
}
