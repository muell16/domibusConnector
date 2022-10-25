package eu.ecodex.dc.core;

import eu.ecodex.dc.TestApplication;
import org.junit.jupiter.api.Test;
import org.moduliths.docs.Documenter;
import org.moduliths.model.Modules;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentationTests {

    @Test
    void writeDocumentationSnippets() throws IOException {
//        ApplicationModules modules = ApplicationModules.of(Application.class);
//        assertThat(Modules.of(TestApplication.class)
//                .detectViolations().hasViolations()).isFalse();

        String customOutputFolder = "target/moduliths";
        Path path = Paths.get(customOutputFolder);

        Documenter d = new Documenter(TestApplication.class);
        d.withOutputFolder(customOutputFolder).writeModuleCanvases();


        d.withOutputFolder(customOutputFolder).writeModulesAsPlantUml(Documenter.Options.defaults().withTargetFileName("modules.plantuml"));
    }
}
