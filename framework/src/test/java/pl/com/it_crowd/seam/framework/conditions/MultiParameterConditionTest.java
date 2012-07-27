package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

public class MultiParameterConditionTest {
// ------------------------------ FIELDS ------------------------------

    final DynamicParameter a_companyIdParam = new DynamicParameter() {
        @Override
        public Object getValue()
        {
            return 3;
        }
    };

    final DynamicParameter a_idParam = new DynamicParameter() {
        @Override
        public Object getValue()
        {
            return 22;
        }
    };

    final DynamicParameter a_nameParam = new DynamicParameter() {
        @Override
        public Object getValue()
        {
            return "Jacek";
        }
    };

    final FreeCondition companyCondition = new FreeCondition("p.company.id=", a_companyIdParam);

    final FreeCondition idCondition = new FreeCondition(a_idParam, "!= id");

    final FreeCondition nameCondition = new FreeCondition(a_nameParam, "=p.owner.name");

    final FreeCondition roleCondition = new FreeCondition(a_nameParam, " in (select name from User where role=admin)");

// -------------------------- OTHER METHODS --------------------------

    @Test
    public void multiDynamicParamTest()
    {
        AbstractCondition condition;

        condition = new OrCondition(new AndCondition(companyCondition, nameCondition), "p.admin = true");
        condition.evaluate();
        Assert.assertEquals("((p.company.id=:qel1 AND :qel2=p.owner.name) OR p.admin = true)", condition.getRenderedEJBQL());
        Assert.assertEquals(2, condition.getParamsToSet().size());

        condition = new OrCondition(new AndCondition(companyCondition, nameCondition, new OrCondition(roleCondition, "true", idCondition)),
            new FreeCondition("p.surname=", a_nameParam));
        condition.evaluate();
        Assert.assertEquals(
            "((p.company.id=:qel1 AND :qel2=p.owner.name AND (:qel3 in (select name from User where role=admin) OR true OR :qel4!= id)) OR p.surname=:qel5)",
            condition.getRenderedEJBQL());
        Assert.assertEquals(5, condition.getParamsToSet().size());
    }

    @Test
    public void multiDynamicParamTest2()
    {
        AbstractCondition condition;

        condition = new OrCondition(new AndCondition(companyCondition, nameCondition), "p.admin = true");
        condition.evaluate();
        Assert.assertEquals("((p.company.id=:qel1 AND :qel2=p.owner.name) OR p.admin = true)", condition.getRenderedEJBQL());
        Assert.assertEquals(2, condition.getParamsToSet().size());

        AbstractCondition condition2 = new OrCondition(new AndCondition(companyCondition, nameCondition, new OrCondition(roleCondition, "true", idCondition)),
            new FreeCondition("p.surname=", a_nameParam));
        condition2.evaluate();

        final int parametersCount = condition.getDynamicParametersCount();
        condition2.setParamIndexOffset(parametersCount + 1);
        condition2.evaluate();
        Assert.assertEquals(
            "((p.company.id=:qel3 AND :qel4=p.owner.name AND (:qel5 in (select name from User where role=admin) OR true OR :qel6!= id)) OR p.surname=:qel7)",
            condition2.getRenderedEJBQL());
        Assert.assertEquals(5, condition2.getParamsToSet().size());
    }

    @Test
    public void multiDynamicParamTest3()
    {
        AbstractCondition condition;
        FakeParameter<User> currentUserParam = new FakeParameter<User>(new User());
        FakeParameter<Boolean> unlockedOnlyParam = new FakeParameter<Boolean>(false);

        condition = new FreeCondition("(p.lockOwner is null or p.lockOwner.id=", currentUserParam, " or false=", unlockedOnlyParam, ")");
        condition.setParamIndexOffset(2);
        condition.evaluate();
        Assert.assertEquals("(p.lockOwner is null or p.lockOwner.id=:qel2 or false=:qel3)", condition.getRenderedEJBQL());
        Assert.assertEquals(2, condition.getDynamicParametersCount());
        Assert.assertEquals(2, condition.getParamsToSet().size());
    }

    private class User {

    }
}
