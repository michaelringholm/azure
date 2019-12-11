package dk.commentor.dal.blobstore;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.ResultSegment;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.azure.storage.blob.CloudBlobClient;

public class AzureBlobService {
    private final CloudStorageAccount storageAccount;
    private final CloudBlobClient blobClient;

    public AzureBlobService(String blobContainerConnString) throws Exception {
        storageAccount = CloudStorageAccount.parse(blobContainerConnString);
        blobClient = storageAccount.createCloudBlobClient();
    }

    public CompletableFuture<ResultSegment<ListBlobItem>> getBlobItems(String blobContainerName, Integer maxResults) throws Exception {
        // BlobListingDetails.SNAPSHOTS        
        return CompletableFuture.supplyAsync(() -> {
            try {
                CloudBlobContainer container = blobClient.getContainerReference(blobContainerName);
                ResultSegment<ListBlobItem> blobItems = container.listBlobsSegmented("", true, null, maxResults, null, null,
                        null);
                return blobItems;
            }
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });        
    }

    public CompletableFuture<byte[]> getBlobContents(String blobContainerName, String blobName) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            CloudBlobContainer container;
            try {
                container = blobClient.getContainerReference(blobContainerName);
                CloudBlockBlob blobBlockRef = container.getBlockBlobReference(blobName);
                blobBlockRef.downloadAttributes();
                byte[] buffer = new byte[(int)blobBlockRef.getProperties().getLength()];       
                blobBlockRef.downloadToByteArray(buffer, 0);
                return buffer;
            } 
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public CompletableFuture<String> getBlobContentsAsString(String blobContainerName, String blobName) throws Exception
    {
        return CompletableFuture.supplyAsync( () -> {
            try {
                byte[] buffer = getBlobContents(blobContainerName, blobName).get();
                String stringBuffer  = new String(buffer, StandardCharsets.UTF_8);
                return stringBuffer;
            } 
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });
    }
        
    public CompletableFuture<Void> putBlobContents(String blobContainerName, String blobName, String jsonData) throws Exception
    {
        return CompletableFuture.runAsync( () -> {
            try {
                CloudBlobContainer container = blobClient.getContainerReference(blobContainerName);
                CloudBlockBlob blobBlockRef = container.getBlockBlobReference(blobName);
                blobBlockRef.downloadAttributes();
                blobBlockRef.uploadText(jsonData);
            } 
            catch (Exception ex) {
                throw new CompletionException(ex);
            }
        });
    }
}
