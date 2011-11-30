package pl.com.it_crowd.seam.framework.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@RequestScoped
@FacesConverter("entityConverter")
public class EntityConverter implements Converter {
// ------------------------------ FIELDS ------------------------------

    @Inject
    private EntityIdentifierStore entityIdentifierStore;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

    public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String id)
    {
        return entityIdentifierStore.get(id);
    }

    public String getAsString(final FacesContext context, final UIComponent comp, final Object entity)
    {
        if (entity != null) {
            return entityIdentifierStore.put(entity);
        }
        return null;
    }
}
