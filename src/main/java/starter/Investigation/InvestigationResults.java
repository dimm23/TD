package starter.Investigation;

import java.util.ArrayList;
import java.util.List;

public class InvestigationResults {
    private List<InvestigateResult> results = new ArrayList<>();

    public InvestigationResults(InvestigateResult results){
        this.results.add(results);
    }

    public List<InvestigateResult> getResults() {
        return this.results;
    }
}
