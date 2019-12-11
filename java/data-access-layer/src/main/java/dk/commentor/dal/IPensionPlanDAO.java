package dk.commentor.dal;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dtl.PensionPlanDTO;

public interface IPensionPlanDAO
{
    CompletableFuture<Void> Store(PensionPlanDTO pensionPlan);
    void BulkStore(List<PensionPlanDTO> pensionPlans, Integer bulkSize);
    CompletableFuture<PensionPlanDTO> GetByCPRNumber(String cprNumber);
    CompletableFuture<List<PensionPlanDTO>> GetByCompanyName(String companyName, Integer maxResults);
}
