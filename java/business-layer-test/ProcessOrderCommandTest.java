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

    public class ProcessOrderCommandTest
    {
        [Fact]
        [Trait("Category","UnitTest")]
        public void Execute() {
            var orderServiceMock = new Mock<IOrderService>();
            var paymentServiceMock = new Mock<IPaymentService>();
            var logger = new Mock<Logger>();

            orderServiceMock.Setup(ps => ps.GetOrder(It.IsAny<String>())).ReturnsAsync(new OrderDTO{});
            orderServiceMock.Setup(ps => ps.ValidateOrder(It.IsAny<OrderDTO>())).ReturnsAsync(true);
            paymentServiceMock.Setup(ps => ps.ProcessPayment(It.IsAny<OrderDTO>())).ReturnsAsync(true);
            
            var processOrderCommand = new ProcessOrderCommand(orderServiceMock.Object, paymentServiceMock.Object, logger.Object);
            var order = processOrderCommand.Execute(new ProcessOrderInputModel{ ShoppingCartId="1"});

            orderServiceMock.Verify(os => os.GetOrder(It.IsAny<String>()));
            paymentServiceMock.Verify(os => os.ProcessPayment(It.IsAny<OrderDTO>()));            
            orderServiceMock.Verify(os => os.ShipOrder(It.IsAny<OrderDTO>()));
        }

        [Fact]
        [Trait("Category","UnitTest")]
        public async void Execute_MissingPayment() {
            var orderServiceMock = new Mock<IOrderService>();
            var paymentServiceMock = new Mock<IPaymentService>();
            var logger = new Mock<Logger>();

            orderServiceMock.Setup(ps => ps.GetOrder(It.IsAny<String>())).ReturnsAsync(new OrderDTO{});
            orderServiceMock.Setup(ps => ps.ValidateOrder(It.IsAny<OrderDTO>())).ReturnsAsync(true);
            paymentServiceMock.Setup(ps => ps.ProcessPayment(It.IsAny<OrderDTO>())).ReturnsAsync(false);
            
            var processOrderCommand = new ProcessOrderCommand(orderServiceMock.Object, paymentServiceMock.Object, logger.Object);
             await Assert.ThrowsAsync<Exception>(() => processOrderCommand.Execute(new ProcessOrderInputModel{ ShoppingCartId="1"}));
            orderServiceMock.Verify(os => os.GetOrder(It.IsAny<String>()));
            paymentServiceMock.Verify(os => os.ProcessPayment(It.IsAny<OrderDTO>()));
        } 
    }
