package pl.com.it_crowd.seam.framework.converter;

import pl.com.it_crowd.seam.framework.Identifiable;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@RequestScoped
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {
// ------------------------------ FIELDS ------------------------------

    private static final String NULL_ENTITY = "null";

    private static final String TRANSIENT_ENTITY = "new";

    @Inject
    private EntityManager entityManager;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        Class entityClass = getEntityClass(context, component);
        if (NULL_ENTITY.equals(value)) {
            return null;
        } else if (TRANSIENT_ENTITY.equals(value)) {
            try {
                return entityClass.newInstance();
            } catch (Exception e) {
                throw new ConverterException("Cannot instantiate new object of type " + entityClass.getCanonicalName());
            }
        } else {
            return entityManager.find(entityClass, Long.parseLong(value));
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null) {
            return NULL_ENTITY;
        }
        if (!(value instanceof Identifiable)) {
            throw new ConverterException(String.format("Class %s doesn't implemente Indentifiable<Long> interface", value.getClass()));
        }
        Object id = ((Identifiable) value).getId();
        return id == null ? TRANSIENT_ENTITY : id.toString();
    }

    protected Class getEntityClass(FacesContext context, UIComponent component)
    {
        return component.getValueExpression("value").getType(context.getELContext());
    }
}
