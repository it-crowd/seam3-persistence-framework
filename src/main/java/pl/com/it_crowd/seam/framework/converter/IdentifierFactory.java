package pl.com.it_crowd.seam.framework.converter;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class IdentifierFactory implements Serializable {
// -------------------------- OTHER METHODS --------------------------

    public Identifier createIdentifier(Object entity, EntityManager entityManager)
    {
        return new Identifier(entity, entityManager);
    }
}
