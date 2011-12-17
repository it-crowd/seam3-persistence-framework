package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class NotNullConditionTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void allArgumentsOk()
    {
        AbstractCondition condition;

        condition = new NotNullCondition("");
        condition.evaluate();
        Assert.assertEquals(" IS NOT NULL", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new NotNullCondition("x");
        condition.evaluate();
        Assert.assertEquals("x IS NOT NULL", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new NotNullCondition(new DynamicParameter() {
            @Override
            public String getValue()
            {
                return Integer.toString(3);
            }
        });
        condition.evaluate();
        Assert.assertEquals(":qel1 IS NOT NULL", condition.getRenderedEJBQL());
        Assert.assertEquals(1, condition.getParamsToSet().size());
    }

    @Test
    public void emptyArgument()
    {
        AbstractCondition condition;

        condition = new NotNullCondition(null);
        condition.evaluate();
        Assert.assertEquals("null IS NOT NULL", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());

        condition = new NotNullCondition(new DynamicParameter() {
            @Override
            public String getValue()
            {
                return null;
            }
        });
        condition.evaluate();
        Assert.assertEquals("null IS NOT NULL", condition.getRenderedEJBQL());
        Assert.assertEquals(0, condition.getParamsToSet().size());
    }
}
