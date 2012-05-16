package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class LocalDynamicParameterTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void isDirty()
    {
        FakeParameter<Long> parameter = new FakeParameter<Long>(null);
        FreeCondition condition = new FreeCondition(parameter);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());

        parameter.setValue(5L);
        condition.evaluate();
        Assert.assertTrue(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        condition.evaluate();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        parameter.setValue(5L);
        //Param value doesn't change so the condition should NOT be dirty 
        Assert.assertFalse(condition.isDirty());
        condition.evaluate();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        parameter.setValue(6L);
        //Param value does change so the condition should BE dirty
        Assert.assertTrue(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());
        condition.evaluate();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());
    }

// -------------------------- INNER CLASSES --------------------------

    private static class MockDynamicParam<E> implements DynamicParameter<E> {
// ------------------------------ FIELDS ------------------------------

        private E value;

// --------------------- GETTER / SETTER METHODS ---------------------

        @Override
        public E getValue()
        {
            return value;
        }

        public void setValue(E value)
        {
            this.value = value;
        }
    }
}
