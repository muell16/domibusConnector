package eu.domibus.connector.security.cli;

import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * A command line interface for 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(scanBasePackages = "eu.domibus.connector.security")
@PropertySource("classpath:test.properties")
public class AsicContainerCli {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AsicContainerCli.class);
    
    public static void main(String... args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(AsicContainerCli.class, args);
        LOGGER.info("RUN...");
        try {
            run.getBean(CreateAsicContainer.class).run(args);
        } catch (Exception e) {
            LOGGER.error("Exception occured:", e);
            System.exit(2);
        }
    }
    
    public static class Args {
        
        
    }

    @Component
    public static class CreateAsicContainer {

        @Resource(name="domibusConnectorContainerService")
        ECodexContainerService containerService;
        
        @Value("${token.issuer.country:null}")
        String country;
    
        @Value("${token.issuer.service.provider:null}")
        String serviceProvider;
    
        @Value("${token.issuer.aes.value:null}")
        AdvancedSystemType advancedElectronicSystem;
        
        
        Options options = new Options();        
        Option createOption;
        Option mainDocumentFileOption;
        private Option mainXmlFileOption;
        private Option attachmentFilesOption;
        private Option writeAsicContainerToOption;
        private TokenIssuer tokenIssuer;
        
        @PostConstruct
        public void init() {
            
            tokenIssuer = new TokenIssuer();
            tokenIssuer.setCountry(country);
            tokenIssuer.setServiceProvider(serviceProvider);
            tokenIssuer.setAdvancedElectronicSystem(advancedElectronicSystem);
            
            
            createOption = Option.builder("create")
                    .hasArg(false)
                    .build();                    
            options.addOption(createOption);
            
            mainDocumentFileOption = Option
                    .builder("maindoc")
                    .hasArg()
                    .desc("path to the main pdf document")                    
                    .build();            
            options.addOption(mainDocumentFileOption);
            
            mainXmlFileOption = Option
                    .builder("mainxml")
                    .hasArg()
                    .desc("path to the main xml")
                    .build();
            options.addOption(mainXmlFileOption);
            
            
            attachmentFilesOption = Option
                    .builder("attachment")
                    .hasArgs()
                    .desc("attachment file")
                    .build();
            options.addOption(attachmentFilesOption);
            
            writeAsicContainerToOption = Option
                    .builder("out")
                    .hasArg()
                    .desc("Write asic container to this path")
                    .build();
            options.addOption(writeAsicContainerToOption);
            
        }
        

        public void run(String... args) throws Exception {            
            CommandLineParser parser = new DefaultParser();
            
            CommandLine parse = parser.parse(options, args);
            if (parse.hasOption(createOption.getOpt())) {
                //TODO: create container
                BusinessContent businessContent = new BusinessContent();
                
                String mainDocumentFilePath = parse.getOptionValue(mainDocumentFileOption.getOpt());
                if (mainDocumentFilePath == null) {
                    throw new RuntimeException(mainDocumentFilePath+ "must provide a path!");
                } else {
                    DSSDocument mainDoc =  new InMemoryDocument(
                            readFileToByteArray(getFileFromPath(mainDocumentFilePath)), 
                            DomibusSecurityContainer.MAIN_DOCUMENT_NAME + ".pdf", 
                            MimeType.PDF);
                    businessContent.setDocument(mainDoc);
                }
                                
                String[] attachmentFilesPathArray = parse.getOptionValues(attachmentFilesOption.getOpt());
                if (attachmentFilesPathArray != null) {
                    List<String> attachmentFilesPathList = Arrays.asList(attachmentFilesPathArray);
                    for (String filePath : attachmentFilesPathList) {
                        File file = getFileFromPath(filePath);
                        businessContent.addAttachment(new InMemoryDocument(
                            readFileToByteArray(file), 
                            file.getName(),
                            MimeType.fromFile(file)
                        ));
                    }
                }
                
                
                ECodexContainer container = createECodexContainer(businessContent);
                writeAsicContainer(container);
                
            }   
            System.exit(0);
        }
        
        private ECodexContainer createECodexContainer(BusinessContent businessContent) {
            try {
                ECodexContainer container = containerService.create(businessContent, tokenIssuer);
                return container;
            } catch (ECodexException e) {
                throw new RuntimeException("Cannot create ECodex Container!", e);
            }
  
        }
        
        private void writeAsicContainer(ECodexContainer container) {
            String writeToPath = writeAsicContainerToOption.getValue();
            try {            
                if (writeToPath == null) {
                    writeToPath = "target/" + DomibusSecurityContainer.ASICS_CONTAINER_IDENTIFIER + ".asics";
                }
                
                DSSDocument asicDocument = container.getAsicDocument();
                File asicSOutputFile = new File(writeToPath);
                FileOutputStream fileOutStream = new FileOutputStream(asicSOutputFile);
                LOGGER.info("Writing asic container to [{}]", asicSOutputFile.getAbsolutePath());
                StreamUtils.copy(asicDocument.openStream(), fileOutStream);                
            } catch (IOException ioe) {
                String error = String.format("Cannot write to %s", writeToPath);
                LOGGER.error(error);
                throw new RuntimeException(
                        error,
                        ioe);
            } 
        }
        
        private File getFileFromPath(String path) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
            throw new RuntimeException(String.format("File could not be found from %s", path));
        }
        
        public byte[] readFileToByteArray(File file) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                LOGGER.info("Reading content from file [{}]", file);
                return StreamUtils.copyToByteArray(fileInputStream);
            } catch (IOException ioe) {
                throw new RuntimeException(
                        String.format("File could not be read from %s", file), 
                        ioe);
            }
        }
        
    }
}
