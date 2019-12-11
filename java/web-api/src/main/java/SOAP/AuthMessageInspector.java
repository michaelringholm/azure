import System.ServiceModel.Channels;
import SoapCore;

package dk.commentor.starterproject.webapi.SOAPServices {
    public class AuthMessageInspector implements IMessageInspector
    {
        public object AfterReceiveRequest(ref Message message)
        {
            return null;
        }

        public void BeforeSendReply(ref Message reply, object correlationState)
        {
            //throw new System.NotImplementedException();
        }
    }
}