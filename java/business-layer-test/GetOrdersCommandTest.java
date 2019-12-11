import System;
import System.Collections.Generic;
import dk.commentor.bl.bo;
import dk.commentor.bl.command;
import dk.commentor.bl.model;
import dk.commentor.dtl;
import dk.commentor.sl;
import Microsoft.Extensions.Logging;
import Moq;
import Xunit;

package dk.commentor.bl.test;

    public class GetOrdersCommandTest
    {
        [Fact]
        [Trait("Category","UnitTest")]
        public void Execute() {
            var orderServiceMock = new Mock<IOrderService>();            
            var logger = new Mock<Logger>();
            orderServiceMock.Setup(ps => ps.GetOrder(It.IsAny<String>())).ReturnsAsync(new OrderDTO{});          
            var getOrdersCommand = new GetOrdersCommand(orderServiceMock.Object, logger.Object);
            var orders = getOrdersCommand.Execute(null);
            orderServiceMock.Verify(os => os.GetOrders());
        }
    }
