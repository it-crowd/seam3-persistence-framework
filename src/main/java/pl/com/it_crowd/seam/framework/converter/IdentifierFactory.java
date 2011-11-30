package pl.com.it_crowd.seam.framework.converter;

import javax.persistence.EntityManager;

public class IdentifierFactory {
// -------------------------- OTHER METHODS --------------------------

    public Identifier createIdentifier(Object entity, EntityManager entityManager)
    {
        return new Identifier(entity, entityManager);
    }
}
