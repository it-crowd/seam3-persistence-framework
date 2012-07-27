package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class OrConditionTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void allArgumentsEmpty()
    {
        AbstractCondition condition;

        condition = new OrCondition();
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new OrCondition(null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new OrCondition(null, null);
        condition.evaluate();
        Assert.assertEquals("", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new OrCondition(null, new DynamicParameter() {
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
        AbstractCondition condition;

        condition = new OrCondition(null, new AndCondition(new FreeCondition("u.login=", "kazek"), null, "u.company is not null"));
        condition.evaluate();
        Assert.assertEquals("((u.login=kazek AND u.company is not null))", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new OrCondition(new AndCondition(new FreeCondition("u.login=", "kazek"), "u.company is not null"),
            new FreeCondition(new DynamicParameter() {
                @Override
                public String getValue()
                {
                    return null;
                }
            }), "id=3");
        condition.evaluate();
        Assert.assertEquals("((u.login=kazek AND u.company is not null) OR id=3)", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());
    }

    @Test
    public void threeArguments()
    {
        AbstractCondition condition;

        condition = new OrCondition(1, 2, 3);
        condition.evaluate();
        Assert.assertEquals("(1 OR 2 OR 3)", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new OrCondition(new AndCondition(new FreeCondition("u.login=", "kazek"), "u.company is not null"),
            new FreeCondition(new DynamicParameter() {
                @Override
                public String getValue()
                {
                    return "5";
                }
            }, " is null"), "id=3");
        condition.evaluate();
        Assert.assertEquals("((u.login=kazek AND u.company is not null) OR :qel1 is null OR id=3)", condition.getRenderedEJBQL());
        Assert.assertEquals(1, condition.getParamsToSet().size());
    }
}
