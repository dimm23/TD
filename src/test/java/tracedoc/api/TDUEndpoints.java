package tracedoc.api;

import static tracedoc.settings.settings.base_url;

public enum TDUEndpoints {
    UNIQUIZE(base_url + "/uniquize"),
    RECOGNIZE(base_url+ "/recognize"),
    INVESTIGATE(base_url + "/investigate"),
    VERSION(base_url + "/management/version"),
    HEALTH(base_url + "/management/health"),

    POST_DOCUMENT(base_url + "/external/v1/tasks/documents"),
    GET_POSTED_DOC_STATUS(base_url + "/external/v1/tasks/%s/documents"),
    MAKE_DOC_UNIQ(base_url + "/external/v1/tasks/documents/uniq"),
    GET_UNIQ_DOC_STATUS(base_url + "/external/v1/tasks/%s/documents/uniq"),
    GET_UNIQ_DOC(base_url + "/external/v1/documents/%s/uniq"),
    GET_PAGE_OF_UNIQ_DOC(base_url + "/external/v1/documents/%s/uniq/pages/%d/%s");


    private final String url;

    TDUEndpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
