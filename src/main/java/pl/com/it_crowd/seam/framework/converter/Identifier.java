package pl.com.it_crowd.seam.framework.converter;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Identifier implements Serializable {
// ------------------------------ FIELDS ------------------------------

    protected Class clazz;

    protected Object id;

// -------------------------- STATIC METHODS --------------------------

    protected static Class getEntityClass(Class clazz)
    {
        while (clazz != null && !Object.class.equals(clazz)) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                return clazz;
            } else {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Override this method if you want to create specialized subclass
     *
     * @param entity
     *
     * @return
     */
    protected static Object unproxy(Object entity)
    {
        return entity;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public Identifier(Object entity, EntityManager entityManager)
    {
        this(getEntityClass(entity.getClass()), entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(unproxy(entity)));
    }

    public Identifier(Class clazz, Object id)
    {
        if (clazz == null || id == null) {
            throw new IllegalArgumentException("Id and clazz must not be null");
        }
        this.clazz = clazz;
        this.id = id;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public Class getClazz()
    {
        return clazz;
    }

    public Object getId()
    {
        return id;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Identifier) {
            Identifier that = (Identifier) other;
            if (id == null || clazz == null) {
                throw new IllegalArgumentException("Class and Id must not be null");
            } else {
                return this.getId().equals(that.getId()) && this.getClazz().equals(that.getClazz());
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings({"unchecked"})
    public Object find(EntityManager entityManager)
    {
        return unproxy(entityManager.find(getClazz(), getId()));
    }
}
