package tracedoc.api.JsonObjects;

public class UniquizeUploadedDoc {
    private String externalDocumentId;
    private String userId;

    public UniquizeUploadedDoc(String externalDocumentId, String userId){
        this.externalDocumentId = externalDocumentId;
        this.userId = userId;
    }

    public String getExternalDocumentId() {return this.externalDocumentId;}
    public String getUserId() {return this.userId;}
}
