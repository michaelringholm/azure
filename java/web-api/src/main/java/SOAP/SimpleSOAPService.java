import System;
import dk.commentor.dtl;
import Microsoft.AspNetCore.Authorization;
import Microsoft.AspNetCore.Http;

package dk.commentor.starterproject.webapi.SOAPServices {
    public class SimpleSOAPService implements ISimpleSOAPService
    {
        private static int constructorCalls = 0;
        public SimpleSOAPService() {
            constructorCalls++;
        }
        public String Echo(String msg)
        {
            return msg;
        }

        public String Ping()
        {
            return "Ping!";
        }

        [Authorize]
        public String SecurePing()
        {            
            return "This is a very secret ping message, shhhh...";
        }        

        public GetOrderOutputModel GetOrder()
        {
            return new GetOrderOutputModel{ Order = new OrderDTO{ ID = Guid.NewGuid(), OrderDate = DateTime.Now }};
        }        
    }
}