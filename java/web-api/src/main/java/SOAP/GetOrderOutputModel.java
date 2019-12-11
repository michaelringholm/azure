import System.Runtime.Serialization;
import dk.commentor.dtl;

package dk.commentor.starterproject.webapi.SOAPServices {
    [DataContract]
    public class GetOrderOutputModel
    {
        [DataMember] public OrderDTO Order;
    }
}