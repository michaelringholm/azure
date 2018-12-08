using System.Collections.Generic;
using System.Diagnostics;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using Microsoft.Extensions.Configuration;
using System;
using System.IO;

namespace com.opusmagus.cloud.blobs {
    public class AzureBlobService : IBlobService {

        public AzureBlobService() {

        }
        public AzureBlobService(IConfiguration config) {

        }

        public IEnumerable<IListBlobItem> getBlobItems(string blobContainerConnString, string blobContainerName, int maxResults)
        {
            var storageAccount = CloudStorageAccount.Parse(blobContainerConnString);
            var blobClient = storageAccount.CreateCloudBlobClient();
            var container = blobClient.GetContainerReference(blobContainerName);
            var blobItems = container.ListBlobsSegmentedAsync("", true, BlobListingDetails.All, maxResults, null, null, null).ConfigureAwait(false).GetAwaiter().GetResult().Results;            
            return blobItems;
        }        

        public byte[] getBlobContents(string blobContainerConnString, string blobContainerName, string blobName) {
            var storageAccount = CloudStorageAccount.Parse(blobContainerConnString);
            var blobClient = storageAccount.CreateCloudBlobClient();
            var container = blobClient.GetContainerReference(blobContainerName);
            var blobBlockRef = container.GetBlockBlobReference(blobName);
            blobBlockRef.FetchAttributesAsync().ConfigureAwait(false).GetAwaiter().GetResult();
            byte[] buffer = new byte[blobBlockRef.Properties.Length];       
            blobBlockRef.DownloadToByteArrayAsync(buffer, 0).ConfigureAwait(false).GetAwaiter().GetResult();
            return buffer;
        }

        private byte[] getBlobContentsOld(string blobContainerConnString, string blobContainerName)
        {
            //var urls = new List<string>();            
            //string blobContainerName = "sample-blob-container";
            var storageAccount = CloudStorageAccount.Parse(blobContainerConnString);
            var blobClient = storageAccount.CreateCloudBlobClient();
            var container = blobClient.GetContainerReference(blobContainerName);
            var blobItems = container.ListBlobsSegmentedAsync("", true, BlobListingDetails.All, 200, null, null, null).ConfigureAwait(false).GetAwaiter().GetResult().Results;
            foreach(var blobItem in blobItems) {
                Debug.WriteLine($"blobUri:{blobItem.Uri}");
                var blockBlob = (CloudBlockBlob)blobItem;
                byte[] buffer = new byte[blockBlob.Properties.Length];                
                var blobRef = container.GetBlobReference(blobItem.Uri.ToString());
                //blockBlob.DownloadToFileAsync($"./local/{Guid.NewGuid()}.docx", FileMode.CreateNew).ConfigureAwait(false).GetAwaiter().GetResult();
                blockBlob.DownloadToByteArrayAsync(buffer, 0).ConfigureAwait(false).GetAwaiter().GetResult();
                return buffer;
            }
            
            return null;
        }
    }
}