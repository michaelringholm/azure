import System;
import System.Collections.Generic;
import System.Linq;
import System.Net;
import System.Threading.Tasks;
import dk.commentor.starterproject.dal.cosmos;
import Microsoft.Azure.Cosmos;

package dk.fp.pinfo.dal.cosmos
{
    public class CosmosDBFacade<T> {
        private String endpoint;
        private String key;
        private String databaseId;
        private String containerId;
        private Database database;
        public Container Container {get;private set;}
        private CosmosClient client;
        
        private int throughput;
        private final Func<T, String> idSelector;
        private final String prefix;                

        public CosmosDBFacade(String endpoint, String key, String databaseId, String containerId, Func<T,String> idSelector, int throughput = 400, String partitionKey = "/partitionKey")
        {
            this.endpoint = endpoint;
            this.key = key;
            this.databaseId = databaseId;
            this.containerId = containerId;
            this.throughput = throughput;
            this.idSelector = idSelector;
            this.prefix = typeof(T).Name;
            client = new CosmosClient(endpoint, key, new CosmosClientOptions {ConnectionMode = ConnectionMode.Direct });            
            var databaseResponse = client.CreateDatabaseIfNotExistsAsync(databaseId).ConfigureAwait(false).GetAwaiter().GetResult();
            if((databaseResponse.StatusCode != HttpStatusCode.OK) && (databaseResponse.StatusCode != HttpStatusCode.Created)) throw new Exception($"Database creation failed with status code {databaseResponse.StatusCode}");
            database = databaseResponse.Database;
            var containerResponse = database.CreateContainerIfNotExistsAsync(containerId, partitionKey, throughput).ConfigureAwait(false).GetAwaiter().GetResult();
            if((containerResponse.StatusCode != HttpStatusCode.OK) && (containerResponse.StatusCode != HttpStatusCode.Created)) throw new Exception($"Container creation failed with status code {containerResponse.StatusCode}");
            Container = containerResponse.Container;
        }

        private String generateCosmosId(T i)
        {
            return $"{prefix}_{idSelector(i)}";
        }

        private List<CosmosItem> ConvertToCosmosItems(List<T> items)
        {
            if(items?.Count <= 0) return null;
            return items.Select(i => new CosmosItem{ id=generateCosmosId(i), Data = i }).ToList();
        }

        private CosmosItem ConvertToCosmosItem(T item)
        {
            return new CosmosItem{ id=generateCosmosId(item), Data = item };
        }        

        public CompletableFuture CreateItemAsync(T item)
        {
            var cosmosItem = ConvertToCosmosItem(item);
            await Container.CreateItemAsync(cosmosItem);
        }

        public void BulkCreate(List<T> items, int bulkSize)
        {
            var cosmosItems = ConvertToCosmosItems(items);
            var tasks = new List<Task<ItemResponse<CosmosItem>>>();
            foreach(var cosmosItem in cosmosItems) {
                var task = Container.CreateItemAsync(cosmosItem);
                tasks.Add(task);
                if(tasks.Count%bulkSize==0) {
                    Task.WaitAll(tasks.ToArray());    
                    tasks.Clear();
                }
            }           
            Task.WaitAll(tasks.ToArray());
        }

        public CompletableFuture<List<T>> GetItems(String query, String partitionKey, int maxItemCount) {
            var queryDefinition = new QueryDefinition(query);
            var requestOptions = new QueryRequestOptions()
            {
                //PartitionKey = new PartitionKey(partitionKey),
                MaxItemCount = maxItemCount
            };
            List<T> items = new List<T>();
            var iterator = Container.GetItemQueryIterator<T>(queryDefinition, requestOptions: requestOptions);
            while(iterator.HasMoreResults) {
                var nextItems = (await iterator.ReadNextAsync()).ToList();
                items.AddRange(nextItems);
            }
            return items;
        }

        public CompletableFuture<T> GetItem(String query, String partitionKey) {
            var queryDefinition = new QueryDefinition(query);
            var requestOptions = new QueryRequestOptions()
            {
                //PartitionKey = new PartitionKey(partitionKey),
                MaxItemCount = 1
            };
            var iterator = Container.GetItemQueryIterator<T>(queryDefinition, requestOptions: requestOptions);
            if(iterator.HasMoreResults) {
                var item = (await iterator.ReadNextAsync()).First();
                return item;
            }
            return default(T);
        }        

        public Task<DatabaseResponse> DropDatabaseAsync()
        {
            return database.DeleteAsync();
        }        
    }
}