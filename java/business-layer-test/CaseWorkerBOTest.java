import System;
import System.Collections.Generic;
import System.Threading.Tasks;
import dk.commentor.bl.bo;
import dk.commentor.dal;
import dk.commentor.dtl;
import dk.commentor.sl;
import Moq;
import Xunit;

package dk.commentor.bl.test;

    public class CaseWorkerBOTest
    {
        [Fact]
        [Trait("Category","UnitTest")]
        public CompletableFuture GetShipCertificate() {
            var certificateDAOMock = new Mock<ICertificateDAO>();
            certificateDAOMock.Setup((f) => f.Restore("5555")).ReturnsAsync(new CertificateDTO {IMONumber="5555"});
            var caseWorkerBO = new CaseWorkerBO(certificateDAOMock.Object, null);
            var imoNumber = await caseWorkerBO.GetShipCertificate("5555");
            Assert.Equal("5555", imoNumber?.IMONumber);
        }

        [Fact]
        [Trait("Category","UnitTest")]
        public void GetShipDocuments() {
            var documentServiceMock = new Mock<IDocumentService>();
            var shipMockDocuments = new List<DocumentDTO>();
            shipMockDocuments.Add(new DocumentDTO{Author = "John Doe"});
            shipMockDocuments.Add(new DocumentDTO{Author = "Jane Doe"});
            documentServiceMock.Setup(f => f.GetDocuments("IMONumber", "5555")).Returns(shipMockDocuments);
            var caseWorkerBO = new CaseWorkerBO(null, documentServiceMock.Object);
            var shipDocuments = caseWorkerBO.GetShipDocuments("5555");
            Assert.Equal(2, shipDocuments.Count);
            Assert.Equal("Jane Doe", shipDocuments[1].Author);
        }             
    }
