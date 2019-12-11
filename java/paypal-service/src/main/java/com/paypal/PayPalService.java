package com.paypal;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import dk.commentor.dtl.OrderDTO;
import dk.commentor.sl.IPaymentService;

    public class PayPalService implements IPaymentService
    {
        private Logger logger;

        public PayPalService(Logger logger) {
            this.logger = logger;
        }

        public CompletableFuture<Boolean> ProcessPayment(OrderDTO order)
        {
            logger.info("PayPal processing payment...");
            return new CompletableFuture<Boolean>();
        }
    }

