package eu.domibus.connector.common;

public class NationalImplementationFactory {

    protected String implementationClassName;

    public void setImplementationClassName(String implementationClassName) {
        this.implementationClassName = implementationClassName;
    }

    public Object createNationalImplementation() throws Exception {
        if (implementationClassName == null || implementationClassName.isEmpty()) {
            throw new IllegalArgumentException(
                    "There is no name for an implementation class given. Please check your configuration!");
        }

        try {
            Object impl = Class.forName(implementationClassName).getConstructor().newInstance();
            return impl;
        } catch (Exception e) {
            throw new Exception(
<<<<<<< HEAD
                    "Could not create an instance for DomibusConnectorContentMapper implementation with full qualified class name "
=======
                    "Could not create an instance for ECodexContentMapper implementation with full qualified class name "
>>>>>>> branch 'development' of https://riederb@secure.e-codex.eu/gitblit/git/e-CODEX/WP5/connector.git
                            + implementationClassName, e);
        }
    }

}
