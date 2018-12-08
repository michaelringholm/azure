using System.Collections.Generic;
using Microsoft.WindowsAzure.Storage.Blob;

namespace com.opusmagus.cloud.blobs
{
    public interface IBlobService
    {
        byte[] getBlobContents(string blobContainerConnString, string blobContainerName, string blobName);
        IEnumerable<IListBlobItem> getBlobItems(string blobContainerConnString, string blobContainerName, int maxResults);
    }
}