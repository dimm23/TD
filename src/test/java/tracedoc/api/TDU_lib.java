package tracedoc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.junit.Before;
import org.junit.Rule;
import starter.Investigation.InvestigateResult;
import starter.Investigation.InvestigationResults;
import tracedoc.api.JsonObjects.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static tracedoc.api.TDUEndpoints.*;
import static tracedoc.settings.settings.*;

public class TDU_lib {
    public Header auth_header = new Header("Authorization", "Basic " + auth_key);
    public Header appJson = new Header("Content-Type", "application/json");
    public Headers headers = new Headers(auth_header, appJson);

    @Step
    public void postDocument(String filePath, String externalDocumentId){
        SerenityRest.given().contentType("multipart/form-data")
                    .multiPart("file", new File(filePath))
                    .multiPart("fileName", new File(filePath).getName())
                    .multiPart("externalDocumentId", externalDocumentId)
                    .header(auth_header).when().post(POST_DOCUMENT.getUrl());
    }
    @Step
    public void checkPostedDocStatus(String taskId){
        SerenityRest.given().headers(headers).when().get(String.format(GET_POSTED_DOC_STATUS.getUrl(), taskId));
    }
    @Step
    public void unicuizeUploadedDoc(String externalDocumentId, String userId){
        UniquizeUploadedDoc uploadedDoc = new UniquizeUploadedDoc(externalDocumentId, userId);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Path fileName = Path.of("target/uniquizeUploadedDoc.json");
            objectMapper.writeValue(new File(String.valueOf(fileName)), uploadedDoc);
            SerenityRest.given().body(Files.readString(fileName)).headers(headers).when().post(MAKE_DOC_UNIQ.getUrl());
        } catch (Exception e){e.printStackTrace();}
    }
    @Step
    public void getDocUniquizeStatus(String taskId){
        SerenityRest.given().auth().basic(username, password).get(String.format(GET_UNIQ_DOC_STATUS.getUrl(), taskId));
    }
    @Step
    public void getUniquizedDocCopy(String documentId){
        SerenityRest.given().auth().basic(username, password).get(String.format(GET_UNIQ_DOC.getUrl(), documentId));
    }
    @Step
    public void getPageOfUniquizedDocCopy(String documentId, int pageNumber, String imageType){
        SerenityRest.given().header(auth_header).get(String.format(GET_PAGE_OF_UNIQ_DOC.getUrl(),
                documentId, pageNumber, imageType));
    }
}
