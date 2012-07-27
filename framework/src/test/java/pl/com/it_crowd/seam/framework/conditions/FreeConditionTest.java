package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class FreeConditionTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void allArgumentsOk()
    {
        FreeCondition condition;

        condition = new FreeCondition("u.login=", "jan", " and u.login!=", new DynamicParameter() {
            @Override
            public String getValue()
            {
                return "kazek";
            }
        });
        condition.evaluate();
        Assert.assertEquals("u.login=jan and u.login!=:qel1", condition.getRenderedEJBQL());
        Assert.assertEquals(1, condition.getParamsToSet().size());
    }

    @Test
    public void emptyArgument()
    {
        FreeCondition condition;

        condition = new FreeCondition();
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new FreeCondition(null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new FreeCondition("u.login=", null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new FreeCondition("u.login=", new DynamicParameter() {
            @Override
            public String getValue()
            {
                return null;
            }
        });
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());
    }
}
