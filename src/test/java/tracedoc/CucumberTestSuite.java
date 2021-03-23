package tracedoc;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.junit.annotations.Concurrent;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@Concurrent(threads = "4")
@CucumberOptions(
        plugin = {"pretty"},
        tags = "@load_test_of_file_uploading",
        features = "src/test/resource/features",
        glue = "tracedoc.stepdefinitions"
)
public class CucumberTestSuite {}
