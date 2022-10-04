package eu.domibus.connector;


import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.OpenShiftClient;
import io.fabric8.openshift.client.OpenShiftExtensionAdapter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class TestConnectorDeploymentKT {

    public static void main(String[] args) throws IOException {


        Config config = new ConfigBuilder()
                .withHttpProxy(null)
                .withHttpsProxy(null)
//                .withNoProxy("*.my.domain.com") // does not work
                .build();

        final OpenShiftExtensionAdapter ocAdapter = new OpenShiftExtensionAdapter();
        try (final OpenShiftClient client = ocAdapter.adapt(new KubernetesClientBuilder().withConfig(config).build())) {
            final String namespace = client.getNamespace();

            if (!namespace.equals("ju-eu-ejustice-eqs")) {
                throw new RuntimeException(String.format("The namespace must be 'ju-eu-ejustice-eqs', but was '%s'", namespace));
            }

//            deleteDeploymentAndPersistentVolumeClaims(client, "03");

            buildLab(client, namespace);

        }

    }


    private static void deleteDeploymentAndPersistentVolumeClaims(OpenShiftClient client, String labId) {
        final List<Deployment> labResources = client.apps().deployments().list().getItems().stream().filter(s -> s.getMetadata().getName().contains(labId)).collect(Collectors.toList());
        client.resourceList(labResources).delete();

        final List<PersistentVolumeClaim> labPvcs = client.persistentVolumeClaims().list().getItems().stream().filter(pvc -> pvc.getMetadata().getName().contains(labId)).collect(Collectors.toList());
        client.resourceList(labPvcs).delete();

        final List<Route> routes = client.routes().list().getItems().stream().filter(r -> r.getMetadata().getName().contains(labId)).collect(Collectors.toList());
        client.resourceList(routes).delete();

    }

    private static void buildLab(KubernetesClient client, String namespace) throws IOException {

        Properties p = new Properties();
        p.put("lab.id", "03");
        p.put("connector.image.version", "latest");

        List<HasMetadata> result = new ArrayList<>();

        result.addAll(client.load(loadResourceDefinition(new ClassPathResource("/k8s/templates/ecx-connector.yaml"), p)).get());
        result.addAll(client.load(loadResourceDefinition(new ClassPathResource("/k8s/templates/ecx-connector-client.yaml"), p)).get());
        result.addAll(client.load(loadResourceDefinition(new ClassPathResource("/k8s/templates/ecx-gateway.yaml"), p)).get());

        client.resourceList(result).inNamespace(namespace).createOrReplace();

    }

    private static InputStream loadResourceDefinition(Resource r, Properties p) throws IOException {
        String ecxConnectorClient = StreamUtils.copyToString(r.getInputStream(), StandardCharsets.UTF_8);
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
        ecxConnectorClient = propertyPlaceholderHelper.replacePlaceholders(ecxConnectorClient, p);
        return new ByteArrayInputStream(ecxConnectorClient.getBytes(StandardCharsets.UTF_8));
    }

}
