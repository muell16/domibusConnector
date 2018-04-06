package eu.domibus.webadmin.spring.support;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

/**
 * Custom JSF ResourceHandler.
 *
 * This handler bridges between Spring MVC and JSF managed resources. The
 * handler takes care of the case when a JSF facelet is used as a view by a
 * Spring MVC Controller and the view uses components like h:outputScript and
 * h:outputStylesheet by correctly pointing the resource URLs generated to the
 * JSF resource handler.
 *
 * The reason this custom handler wrapper is needed is because the JSF internal
 * logic assumes that the request URL for the current page/view is a JSF url. If
 * it is a Spring MVC request, JSF will create URLs that incorrectly includes
 * the Spring controller context.
 *
 * This handler will strip out the Spring context for the URL and add the ".jsf"
 * suffix, so the resource request will be routed to the FacesServlet with a
 * correct resource context (assuming the faces servlet is mapped to the *.jsf
 * pattern).
 *
 *
 */
public class CustomResourceHandler extends ResourceHandlerWrapper {

    private ResourceHandler wrapped;

    public CustomResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        return new CustomResource(super.createResource(resourceName, libraryName));
    }

    @Override
    public Resource createResource(String resourceName, String libraryName,
            String contentType) {
        return new CustomResource(super.createResource(resourceName, libraryName, contentType));
    }

    private static class CustomResource extends ResourceWrapper {

        private Resource wrapped;

        private CustomResource(Resource wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Resource getWrapped() {
            return this.wrapped;
        }

        @Override
        public String getRequestPath() {
            String path = super.getRequestPath();
            FacesContext context = FacesContext.getCurrentInstance();
            String facesServletMapping = Util.getFacesMapping(context);
            // if prefix-mapped, this is a resource that is requested from a faces page
            // rendered as a view to a Spring MVC controller.
            // facesServletMapping will, in fact, be the Spring mapping
            if (Util.isPrefixMapped(facesServletMapping)) {
                // remove the Spring mapping
                path = path.replaceFirst("(" + facesServletMapping + ")/", "/");
                // append .jsf to route this URL to the FacesServlet
                path = path.replace(wrapped.getResourceName(), wrapped.getResourceName() + ".jsf");
            }
            return path;
        }

    }
}
