package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class AndConditionTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void allArgumentsEmpty()
    {
        AndCondition condition;

        condition = new AndCondition();
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new AndCondition(null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new AndCondition(null, null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new AndCondition(null, new DynamicParameter() {
            @Override
            public String getValue()
            {
                return null;
            }
        }, null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());
    }

    @Test
    public void oneEmptyArgument()
    {
        AndCondition condition;

        condition = new AndCondition(new FreeCondition("u.login=", "kazek"), null, "u.company is not null");
        condition.evaluate();
        Assert.assertEquals("(u.login=kazek AND u.company is not null)", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new AndCondition(new FreeCondition("u.login=", "kazek"), new FreeCondition(new DynamicParameter() {
            @Override
            public String getValue()
            {
                return null;
            }
        }, " is not null"), "u.company is not null");
        condition.evaluate();
        Assert.assertEquals("(u.login=kazek AND u.company is not null)", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());
    }
}
