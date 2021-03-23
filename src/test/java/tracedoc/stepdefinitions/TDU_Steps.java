package tracedoc.stepdefinitions;

import com.ibm.icu.text.Transliterator;
import io.cucumber.java.ru.*;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import tracedoc.api.TDU_lib;
import tracedoc.text_generator.TextGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class TDU_Steps {
    @Steps
    TDU_lib tdu;

    @Когда("документ {string} поступил в систему документооборота")
    public void documentUploadedToTheSystem(String fileName) {
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
        // Загружаем файл в систему с помощью REST запроса
        tdu.postDocument(filePath, externalDocumentId);
        // Проверяем что ответ приходит со статусом 201
        assert SerenityRest.lastResponse().getStatusCode() == 201;
        // Извлекаем из ответа taskId
        String taskId = SerenityRest.lastResponse().getBody().jsonPath().get("taskId");
        // С помощью полученного taskId начинаем проверять статус обработки документа системой
        // и ожидаем что статус перестанет быть IN_PROGRESS
        String status = "IN_PROGRESS";
        while (status.equals("IN_PROGRESS")) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            tdu.checkPostedDocStatus(taskId);
            status = SerenityRest.lastResponse().getBody().jsonPath().get("status");
        }
    }

    @И("{string} делает уникальную копию и скачивает документ {string}")
    public void userUniquizeDocAndDownloadIt(String userName, String fileName) {
        // Формируем userId и externalDocumentId
        String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
        String userId = "";
        try {
            // Если в тесте передаётся пользователь русскими буквами, то с помощью Translitertor преобразуем его в ENG буквы
            Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
            userId = toLatinTrans.transliterate(userName).replace(" ", "")
                    .replace(".", "").toLowerCase();
        } catch (Exception e) {
            userId = userName.replace(" ", "").replace(".", "").toLowerCase();
        }

        // Получаем все файлы из папки сохранения и берём тот в названии которого есть заданное имя
        // Это будет файл, который только что загрузили в систему
        // и далее формируем из его названия externalDocumentId
        String currentDir = System.getProperty("user.dir");
        String[] fileNames = new File(currentDir + "\\src\\test\\files\\tdu\\").list();
        String externalDocumentId = "";
        assert fileNames != null;
        for (String fName : fileNames) {
            if (fName.contains(fileName)) {
                externalDocumentId = fName.replace("docx", "").replace(" ", "")
                        .replace(".", "").toLowerCase();
            }
        }

        // Уникализируем документ
        tdu.unicuizeUploadedDoc(externalDocumentId, userId);

        assert SerenityRest.lastResponse().getStatusCode() == 201;
        String taskId = SerenityRest.lastResponse().getBody().jsonPath().get("taskId");

        // Ожидаем окончания процесса уникализации
        String status = "IN_PROGRESS";
        while (status.equals("IN_PROGRESS")){
            try { Thread.sleep(1000); } catch (Exception ignored) {}
            tdu.getDocUniquizeStatus(taskId);
            status = SerenityRest.lastResponse().getBody().jsonPath().get("status");
        }

        // Скачиваем уникализированную копию
        String documentId = SerenityRest.lastResponse().getBody().jsonPath().get("documentId");
        tdu.getUniquizedDocCopy(documentId);
        List<String> responseHeaderValues = SerenityRest.lastResponse()
                                                        .getHeaders()
                                                        .getValues("Content-Disposition");

        File file = new File(System.getProperty("user.dir") + "\\src\\test\\files\\tdu\\"
                + responseHeaderValues.get(0).substring(21));
        try {
            Files.write(Path.of(file.getPath()), SerenityRest.lastResponse().asByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Дополнительно скачиваем 1 страницу документа в формате PNG, чтобы проверить что метод работает
        tdu.getPageOfUniquizedDocCopy(documentId, 1, "PNG");
        responseHeaderValues = SerenityRest.lastResponse().getHeaders().getValues("Content-Disposition");

        // Очищаем папку с изображениями документов
        String[] filesIn1 = new File(System.getProperty("user.dir") + "\\src\\test\\files\\tdu\\1").list();
        if (filesIn1 != null) {
            for (String pngFileName : filesIn1) {
                try {
                    Files.deleteIfExists(Path.of(System.getProperty("user.dir")
                            + "\\src\\test\\files\\tdu\\1\\" + pngFileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Если папки нету, то создаём её
        try {
            Files.createDirectory(Path.of(System.getProperty("user.dir") + "\\src\\test\\files\\tdu\\1"));
        } catch (IOException e) { e.printStackTrace(); }

        // Записываем в папку полученные данные в файл
        File file1 = new File(System.getProperty("user.dir") + "\\src\\test\\files\\tdu\\1\\"
                + responseHeaderValues.get(0).substring(21));
        try {
            Files.write(Path.of(file1.getPath()), SerenityRest.lastResponse().asByteArray());
        } catch (IOException e) { e.printStackTrace(); }

        int requestStatus = SerenityRest.lastResponse().getStatusCode();
        if (requestStatus != 200) {
            System.out.println("=== get page of Uniquized Doc copy return status: " + requestStatus + " ===");
        }
    }

    @Тогда("скаченные документы являются уникальными")
    public void downloadedDocAreUniqal() {
        // Получаем названия всех файлов в указанной папке
        String currentDir = System.getProperty("user.dir");
        String[] fileNames = new File(currentDir + "\\src\\test\\files\\tdu\\").list();
        List<Integer> filesHashes = new ArrayList<>();
        assert fileNames != null;
        for (String fileName : fileNames) {
            // Проверяем если в названии файла есть указанный текст, значит это не то что нам нужно и мы его удаляем
            if (fileName.contains("New_file")) {
                try {
                    Files.deleteIfExists(Path.of(currentDir + "\\src\\test\\files\\tdu\\" + fileName));
                } catch (Exception e) {e.printStackTrace();}
            }
            // А если название другое то получаем hash сумму файла и записываем в массив filesHashes
            File file = new File(currentDir + "\\src\\test\\files\\tdu\\" + fileName);
            filesHashes.add(file.hashCode());
        }
        // Заведомо известно что файлов 2шт, поэтому сравниваем хэш сумму первого файла со вторым
        assert !filesHashes.get(0).equals(filesHashes.get(1));
    }

    @Дано("у пользователей нет уникализированных документов")
    public void filesNotExists() {
        // Удаляем все файлы из указанной папки
        String currentDir = System.getProperty("user.dir");
        String[] fileNames = new File(currentDir + "\\src\\test\\files\\tdu\\").list();
        try {
            for (String fName : fileNames) {
                try {
                    Files.deleteIfExists(Path.of(currentDir + "\\src\\test\\files\\tdu\\" + fName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {}
    }
}
