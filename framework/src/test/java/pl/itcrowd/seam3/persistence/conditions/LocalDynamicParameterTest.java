package pl.itcrowd.seam3.persistence.conditions;

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
        condition.markParametersSet();
        Assert.assertEquals("", condition.getRenderedEJBQL());

        parameter.setValue(5L);
        condition.evaluate();
        Assert.assertTrue(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        condition.evaluate();
        condition.markParametersSet();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        parameter.setValue(5L);
        //Param value doesn't change so the condition should NOT be dirty 
        Assert.assertFalse(condition.isDirty());
        condition.evaluate();
        condition.markParametersSet();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());

        parameter.setValue(6L);
        condition.evaluate();
        //Param value does change so the condition should BE dirty
        Assert.assertTrue(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());
        condition.evaluate();
        condition.markParametersSet();
        Assert.assertFalse(condition.isDirty());
        Assert.assertEquals(":qel1", condition.getRenderedEJBQL());
    }
}
