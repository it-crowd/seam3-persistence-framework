package pl.com.it_crowd.seam.framework.converter;

import org.jboss.solder.core.Requires;
import org.jboss.solder.core.Veto;
import pl.com.it_crowd.seam.framework.Identifiable;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

@Veto
@Requires({"javax.persistence.EntityManager", "javax.faces.convert.Converter"})
@RequestScoped
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {
// ------------------------------ FIELDS ------------------------------

    private static final String NULL_ENTITY = "null";

    private static final String TRANSIENT_ENTITY = "new";

    /**
     * This is suppressed because user is supposed to set it i.e. via xml config.
     */
    @SuppressWarnings("UnusedDeclaration")
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
            if (entityManager == null) {
                throw new IllegalStateException("Please configure entityConverter's entityManager");
            }
            //noinspection unchecked
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
