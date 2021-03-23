package starter.Investigation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/investigate/results")
@Api("investigate result")
public class InvestigationController {


    @PostMapping
    @ApiOperation("Return investigateResults result")
    public String results(@RequestBody InvestigationResults investigateResults){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Path filePath = Path.of("target/investigateResults.json");
            objectMapper.writeValue(new File(String.valueOf(filePath)), investigateResults);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Success!"; }
}
