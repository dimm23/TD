package tracedoc.stepdefinitions;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tracedoc.api.TDU_lib;
import tracedoc.text_generator.TextGenerator;

public class scriptSteps {
    @Steps
    TDU_lib tdu;

    @Когда("пользователь генерирует docx документ")
    public void generatingOfDocxDocumetn() {
        String fileName = "Generated_doc";
        // Генерируем постфикс к названию файла, чтобы он был уникальным
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        // Генерируем текст для наполнения файла
        TextGenerator textGenerator = new TextGenerator();
        String fileContent = textGenerator.generatedString(100);
        // Формируем название файла, путь к нему и externalDocumentId
        String currentFolder = System.getProperty("user.dir");
        String filePath = currentFolder + "\\src\\test\\files\\tdu\\" + fileName + "_" + generatedString + ".docx";
        String externalDocumentId = (fileName + "_" + generatedString).replace(" ", "")
                .replace(".", "").toLowerCase();
        // Создаём файл и заполняем его сгенерированным текстом
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(filePath)) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(fileContent);
            document.setParagraph(paragraph, 0);
            document.write(out);
        } catch (IOException e) { e.printStackTrace(); }

        System.out.println(fileName + "_" + generatedString + ".docx");
        System.out.println(externalDocumentId);
    }


    @Когда("пользователь генерирует документ и загружает {int} раз")
    public void userGenerateAndUploadDoc(int loops) {
        int failsCount = 0;
        for (int i = 0; i < loops; i++){
            String fileName = "Generated_doc";
            // Генерируем постфикс к названию файла, чтобы он был уникальным
            String generatedString = RandomStringUtils.randomAlphanumeric(10);
            // Генерируем текст для наполнения файла
            TextGenerator textGenerator = new TextGenerator();
            String fileContent = textGenerator.generatedString(100);
            // Формируем название файла, путь к нему и externalDocumentId
            String currentFolder = System.getProperty("user.dir");
            String filePath = currentFolder + "\\src\\test\\files\\tdu\\" + fileName + "_" + generatedString + ".docx";
            String externalDocumentId = (fileName + "_" + generatedString).replace(" ", "")
                    .replace(".", "").toLowerCase();
            // Создаём файл и заполняем его сгенерированным текстом
            try (XWPFDocument document = new XWPFDocument();
                 FileOutputStream out = new FileOutputStream(filePath)) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(fileContent);
                document.setParagraph(paragraph, 0);
                document.write(out);
            } catch (IOException e) { e.printStackTrace(); }

            tdu.postDocument(filePath, externalDocumentId);
            // Проверяем что ответ приходит со статусом 201
            if (SerenityRest.lastResponse().getStatusCode() != 201) {
                failsCount++;
            }
        }
        System.out.println(failsCount + " failed requests from " + loops + " loops");
        assert failsCount == 0;
    }

    @Когда("пользователь запрашивает статус загрузки документа {int} раз")
    public void userCallStatusOfFileUploading(int loops) {
        String taskId = "cba69eb0-3835-4fa6-969b-6e913ed8d17d";
        int failsCount = 0;
        for (int i = 0; i < loops; i++) {
            tdu.checkPostedDocStatus(taskId);
            if (SerenityRest.lastResponse().getStatusCode() != 200) {
                failsCount++;
            }
        }
        System.out.println(failsCount + " failed requests from " + loops + " loop");
        assert failsCount == 0;
    }
}
