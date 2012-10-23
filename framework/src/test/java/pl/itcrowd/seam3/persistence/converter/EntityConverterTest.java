package pl.itcrowd.seam3.persistence.converter;

import org.junit.Test;
import pl.itcrowd.seam3.persistence.Identifiable;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityConverterTest {
// ------------------------------ FIELDS ------------------------------

    FacesContext facesContext;

    UIComponent uiComponent;

// --------------------------- CONSTRUCTORS ---------------------------

    public EntityConverterTest()
    {
        uiComponent = mock(UIComponent.class);
        ValueExpression valueExpression = mock(ValueExpression.class);
        when(valueExpression.getType(any(ELContext.class))).thenReturn(IdentifiableEntity.class);
        when(uiComponent.getValueExpression("value")).thenReturn(valueExpression);
        facesContext = mock(FacesContext.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Test
    public void getAsObjectNonExisingEntity()
    {
//        Given
        IdentifiableEntity entity = new IdentifiableEntity(1L);
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.find(IdentifiableEntity.class, 1L)).thenReturn(entity);
        EntityConverter converter = new EntityConverter();
        converter.setEntityManager(entityManager);

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, "2");

//        Then
        assertNull(result);
    }

    @Test
    public void getAsObjectNonTransientEntity()
    {
//        Given
        IdentifiableEntity entity = new IdentifiableEntity(1L);
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.find(IdentifiableEntity.class, 1L)).thenReturn(entity);
        EntityConverter converter = new EntityConverter();
        converter.setEntityManager(entityManager);

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, "1");

//        Then
        assertEquals(entity, result);
    }

    @Test
    public void getAsObjectNull()
    {
//        Given
        EntityManager entityManager = mock(EntityManager.class);
        EntityConverter converter = new EntityConverter();
        converter.setEntityManager(entityManager);

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, EntityConverter.NULL_ENTITY);

//        Then
        assertNull(result);
    }

    @Test
    public void getAsObjectNullWithCustomString()
    {
//        Given
        EntityManager entityManager = mock(EntityManager.class);
        EntityConverter converter = new EntityConverter();
        converter.setEntityManager(entityManager);
        converter.setNullEntity("nothing");

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, "nothing");

//        Then
        assertNull(result);
    }

    @Test
    public void getAsObjectTransientEntity()
    {
//        Given
        EntityConverter converter = new EntityConverter();

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, EntityConverter.TRANSIENT_ENTITY);

//        Then
        assertNotNull(result);
        assertTrue(result instanceof IdentifiableEntity);
        assertNull(((IdentifiableEntity) result).getId());
    }

    @Test
    public void getAsObjectTransientEntityWithCustomString()
    {
//        Given
        EntityConverter converter = new EntityConverter();
        String transientEntityString = "transient";
        converter.setTransientEntity(transientEntityString);

//        When
        Object result = converter.getAsObject(facesContext, uiComponent, transientEntityString);

//        Then
        assertNotNull(result);
        assertTrue(result instanceof IdentifiableEntity);
        assertNull(((IdentifiableEntity) result).getId());
    }

    @Test
    public void getAsStringNonTransientEntity()
    {
//        Given
        IdentifiableEntity entity = new IdentifiableEntity(1L);
        EntityConverter converter = new EntityConverter();

//        When
        String result = converter.getAsString(facesContext, uiComponent, entity);

//        Then
        assertEquals("1", result);
    }

    @Test
    public void getAsStringNull()
    {
//        Given
        EntityConverter converter = new EntityConverter();

//        When
        String result = converter.getAsString(facesContext, uiComponent, null);

//        Then
        assertEquals(EntityConverter.NULL_ENTITY, result);
    }

    @Test
    public void getAsStringNullWithCustomString()
    {
//        Given
        EntityConverter converter = new EntityConverter();
        String nullEntityString = "nothing";
        converter.setNullEntity(nullEntityString);

//        When
        String result = converter.getAsString(facesContext, uiComponent, null);

//        Then
        assertEquals(nullEntityString, result);
    }

    @Test
    public void getAsStringTransientEntity()
    {
//        Given
        IdentifiableEntity entity = new IdentifiableEntity();
        EntityConverter converter = new EntityConverter();

//        When
        String result = converter.getAsString(facesContext, uiComponent, entity);

//        Then
        assertEquals(EntityConverter.TRANSIENT_ENTITY, result);
    }

    @Test
    public void getAsStringTransientEntityWithCustomString()
    {
//        Given
        IdentifiableEntity entity = new IdentifiableEntity();
        EntityConverter converter = new EntityConverter();
        String transientEntityString = "transient";
        converter.setTransientEntity(transientEntityString);
//        When
        String result = converter.getAsString(facesContext, uiComponent, entity);

//        Then
        assertEquals(transientEntityString, result);
    }

// -------------------------- INNER CLASSES --------------------------

    private static class IdentifiableEntity implements Identifiable<Long> {
// ------------------------------ FIELDS ------------------------------

        private Long id;

// --------------------------- CONSTRUCTORS ---------------------------

        public IdentifiableEntity()
        {

        }

        public IdentifiableEntity(long id)
        {
            setId(id);
        }

// --------------------- GETTER / SETTER METHODS ---------------------

        public Long getId()
        {
            return id;
        }

        public void setId(Long id)
        {
            this.id = id;
        }
    }
}
