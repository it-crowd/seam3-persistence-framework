package pl.com.it_crowd.seam.framework.converter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
public class EntityIdentifierStore implements Serializable {
// ------------------------------ FIELDS ------------------------------

    @Inject
    private EntityManager entityManager;

    @Inject
    private IdentifierFactory factory;

    private List<Identifier> store;

// -------------------------- OTHER METHODS --------------------------

    @PostConstruct
    public void create()
    {
        store = new ArrayList<Identifier>();
    }

    public Object get(String key)
    {
        try {
            Identifier identifier = store.get(new Integer(key));
            return identifier.find(entityManager);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String put(Object entity)
    {
        Identifier identifier = createIdentifier(entity);
        if (!store.contains(identifier)) {
            store.add(identifier);
        }
        return ((Integer) store.indexOf(identifier)).toString();
    }

    private Identifier createIdentifier(Object entity)
    {
        return factory.createIdentifier(entity, entityManager);
    }
}
