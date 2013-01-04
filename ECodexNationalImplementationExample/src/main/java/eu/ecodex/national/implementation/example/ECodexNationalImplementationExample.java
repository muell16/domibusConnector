package eu.ecodex.national.implementation.example;

import java.net.URISyntaxException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ECodexNationalImplementationExample {

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException {

        @SuppressWarnings("unused")
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/context/ECodexNationalImplementationContext.xml");

        System.out.println("ksdkfjsdkf");
    }

}
