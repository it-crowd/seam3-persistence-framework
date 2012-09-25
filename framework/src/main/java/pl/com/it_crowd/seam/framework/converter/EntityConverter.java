package pl.com.it_crowd.seam.framework.converter;

import org.jboss.solder.core.Requires;
import org.jboss.solder.core.Veto;

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
@FacesConverter(value = "pl.com.it_crowd.seam.framework.converter.Entity")
public class EntityConverter implements Converter {
// ------------------------------ FIELDS ------------------------------

    public static final String NULL_ENTITY = "";

    public static final String TRANSIENT_ENTITY = "new";

    /**
     * User may set it i.e. via xml config or convertEntity tag.
     */
    private EntityManager entityManager;

    private String nullEntity = NULL_ENTITY;

    private String transientEntity = TRANSIENT_ENTITY;

// --------------------- GETTER / SETTER METHODS ---------------------

    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public String getNullEntity()
    {
        return nullEntity;
    }

    public void setNullEntity(String nullEntity)
    {
        this.nullEntity = nullEntity;
    }

    public String getTransientEntity()
    {
        return transientEntity;
    }

    public void setTransientEntity(String transientEntity)
    {
        this.transientEntity = transientEntity;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        Class entityClass = getEntityClass(context, component);
        if (nullEntity.equals(value)) {
            return null;
        } else if (transientEntity.equals(value)) {
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
        if (value == null || nullEntity.equals(value)) {
            return nullEntity;
        }
        if (transientEntity.equals(value)) {
            return transientEntity;
        } else if (!(value instanceof pl.com.it_crowd.seam.framework.Identifiable)) {
            throw new ConverterException(String.format("Class %s doesn't implemente Indentifiable<Long> interface", value.getClass()));
        }
        Object id = ((pl.com.it_crowd.seam.framework.Identifiable) value).getId();
        return id == null ? transientEntity : id.toString();
    }

    protected Class getEntityClass(FacesContext context, UIComponent component)
    {
        return component.getValueExpression("value").getType(context.getELContext());
    }
}