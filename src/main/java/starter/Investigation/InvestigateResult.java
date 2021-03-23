package starter.Investigation;


public class InvestigateResult {
    private int uniquizeNumber;
    private int confidence;

    public InvestigateResult() {
        super();
    }

    public InvestigateResult(int uniquizeNumber, int confidence) {
        this.uniquizeNumber = uniquizeNumber;
        this.confidence = confidence;
    }

    public int getUniquizeNumber() {
        return this.uniquizeNumber;
    }

    public int getConfidence() {
        return this.confidence;
    }
}
