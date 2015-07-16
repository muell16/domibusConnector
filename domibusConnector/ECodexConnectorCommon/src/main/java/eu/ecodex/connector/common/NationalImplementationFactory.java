package eu.ecodex.connector.common;

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
                    "Could not create an instance for ECodexContentMapper implementation with full qualified class name "
                            + implementationClassName, e);
        }
    }

}
