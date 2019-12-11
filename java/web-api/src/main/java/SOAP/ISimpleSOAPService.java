import System.ServiceModel;
import Microsoft.AspNetCore.Authorization;
import Microsoft.AspNetCore.Http;

package dk.commentor.starterproject.webapi.SOAPServices {
    [ServiceContract]
    public interface ISimpleSOAPService
    {
        [OperationContract]
        String Ping();

        [OperationContract]
        [Authorize]
        //String SecurePing(HttpContext httpContext);
        String SecurePing();

        [OperationContract]
        String Echo(String msg);

        [OperationContract]
        GetOrderOutputModel GetOrder();
    }
}