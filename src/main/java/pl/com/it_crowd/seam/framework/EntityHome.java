package pl.com.it_crowd.seam.framework;

import org.jboss.seam.persistence.SeamPersistenceProvider;
import org.jboss.seam.transaction.Transactional;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Base class for Home objects of JPA entities.
 *
 * @author Gavin King
 */
@SuppressWarnings({"ManagedBeanInconsistencyInspection"})
public abstract class EntityHome<E> extends Home<EntityManager, E> {
// ------------------------------ FIELDS ------------------------------

    @Inject
    protected BeanManager beanManager;

    @Inject
    protected Instance<EntityManager> entityManagerInstance;

    @Inject
    protected Instance<SeamPersistenceProvider> persistenceProvider;

// -------------------------- OTHER METHODS --------------------------

    /**
     * Implementation of {@link Home#find() find()} for JPA
     *
     * @see Home#find()
     */
    @Transactional
    @Override
    public E find()
    {
        if (getEntityManager().isOpen()) {
            E result = loadInstance();
            if (result == null) {
                result = handleNotFound();
            }
            return result;
        } else {
            return null;
        }
    }

    public EntityManager getEntityManager()
    {
        return entityManagerInstance.get();
    }

    /**
     * Returns true if the entity instance is managed
     */
    @Transactional
    public boolean isManaged()
    {
        return getInstance() != null && getEntityManager().contains(getInstance());
    }

    @Transactional
    public boolean persist()
    {
        getEntityManager().persist(getInstance());
        getEntityManager().flush();
        assignId(persistenceProvider.get().getId(getInstance(), getEntityManager()));
        beanManager.fireEvent(getInstance(), new AnnotationLiteral<EntityPersisted>() {
        });
        return true;
    }

    @Transactional
    public boolean remove()
    {
        getEntityManager().remove(getInstance());
        getEntityManager().flush();
        beanManager.fireEvent(getInstance(), new AnnotationLiteral<EntityRemoved>() {
        });
        return true;
    }

    @Transactional
    public boolean update()
    {
        joinTransaction();
        getEntityManager().flush();
        beanManager.fireEvent(getInstance(), new AnnotationLiteral<EntityUpdated>() {
        });
        return true;
    }

    /**
     * Implementation of {@link Home#getEntityName() getEntityName()} for JPA
     *
     * @see Home#getEntityName()
     */
    @Override
    protected String getEntityName()
    {
        try {
            return persistenceProvider.get().getName(getInstance(), getEntityManager());
        } catch (IllegalArgumentException e) {
            // Handle that the passed object may not be an entity
            return null;
        }
    }

    /**
     * Implementation of {@link Home#joinTransaction() joinTransaction()} for
     * JPA.
     */
    @Override
    protected void joinTransaction()
    {
        if (getEntityManager().isOpen()) {
            try {
                transaction.get().enlist(getEntityManager());
            } catch (javax.transaction.SystemException se) {
                throw new RuntimeException("could not join transaction", se);
            }
        }
    }

    /**
     * Utility method to load entity instance from the {@link EntityManager}.
     * Called by {@link #find()}.
     * <br />
     * Can be overridden to support eager fetching of associations.
     *
     * @return The entity identified by {@link Home#getEntityClass() getEntityClass()},
     *         {@link Home#getId() getId()}
     */
    protected E loadInstance()
    {
        return getEntityManager().find(getEntityClass(), getId());
    }
}

