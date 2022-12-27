package eu.ecodex.dc5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.moduliths.docs.Documenter;
import org.moduliths.test.*;
import org.moduliths.*;
import org.moduliths.model.Modules;
import org.moduliths.model.Modules.Filters;
import org.moduliths.model.Violations;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModularityTest {

    Documenter documenter = new Documenter(DC5FlowModule.class);


//    @Test
//    @Disabled
//    void drawModuleGraph() throws IOException {
//
//        String customOutputFolder = "target/moduliths";
//
//        documenter.withOutputFolder(customOutputFolder)
//                .writeModuleCanvases();
//        documenter.withOutputFolder(customOutputFolder)
//                .writeModulesAsPlantUml(Documenter.Options.defaults().withTargetFileName("components.plantuml"));
//
//    }


}
