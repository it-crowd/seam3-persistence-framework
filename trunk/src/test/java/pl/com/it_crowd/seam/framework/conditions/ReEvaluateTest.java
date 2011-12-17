package pl.com.it_crowd.seam.framework.conditions;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReEvaluateTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void revaluateTest()
    {
        final FakeParameter companyIdParam = new FakeParameter(3);
        final FreeCondition companyCondition = new FreeCondition("p.company.id=", companyIdParam);
        final FakeParameter idParam = new FakeParameter(22);
        final FreeCondition idCondition = new FreeCondition(idParam, "!= id");
        final FakeParameter nameParam = new FakeParameter("jacek");
        final FreeCondition nameCondition = new FreeCondition(nameParam, "=p.owner.name");
        final FreeCondition roleCondition = new FreeCondition(nameParam, " in (select name from User where role=admin)");

        AbstractCondition condition = new OrCondition(new AndCondition(companyCondition, nameCondition, new OrCondition(roleCondition, "true", idCondition)),
            new FreeCondition("p.surname=", nameParam));
        Set<AbstractCondition.LocalDynamicParameter> paramsToSet;
        Map<String, AbstractCondition.LocalDynamicParameter> parameterMap;

        condition.evaluate();
        paramsToSet = condition.getParamsToSet();
        parameterMap = parameterSet2Map(paramsToSet);
        Assert.assertEquals(5, paramsToSet.size());
        Assert.assertEquals(5, parameterMap.size());
        Assert.assertEquals(3, parameterMap.get("qel1").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel2").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel3").getValue());
        Assert.assertEquals(22, parameterMap.get("qel4").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel5").getValue());

        companyIdParam.setValue(4);
        nameParam.setValue("maria");
        idParam.setValue(33);

        /**
         * Dynamic param values may change but until evaluate() is called the values stay unchanged
         */
        paramsToSet = condition.getParamsToSet();
        parameterMap = parameterSet2Map(paramsToSet);
        Assert.assertEquals(5, paramsToSet.size());
        Assert.assertEquals(5, parameterMap.size());
        Assert.assertEquals(3, parameterMap.get("qel1").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel2").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel3").getValue());
        Assert.assertEquals(22, parameterMap.get("qel4").getValue());
        Assert.assertEquals("jacek", parameterMap.get("qel5").getValue());


        condition.evaluate();
        paramsToSet = condition.getParamsToSet();
        parameterMap = parameterSet2Map(paramsToSet);
        Assert.assertEquals(5, paramsToSet.size());
        Assert.assertEquals(5, parameterMap.size());
        Assert.assertEquals(4, parameterMap.get("qel1").getValue());
        Assert.assertEquals("maria", parameterMap.get("qel2").getValue());
        Assert.assertEquals("maria", parameterMap.get("qel3").getValue());
        Assert.assertEquals(33, parameterMap.get("qel4").getValue());
        Assert.assertEquals("maria", parameterMap.get("qel5").getValue());

        nameParam.setValue(null);

        condition.evaluate();
        paramsToSet = condition.getParamsToSet();
        parameterMap = parameterSet2Map(paramsToSet);
        Assert.assertEquals(2, paramsToSet.size());
        Assert.assertEquals(2, parameterMap.size());
        Assert.assertEquals(4, parameterMap.get("qel1").getValue());
        Assert.assertEquals(33, parameterMap.get("qel4").getValue());
    }

    private Map<String, AbstractCondition.LocalDynamicParameter> parameterSet2Map(Set<AbstractCondition.LocalDynamicParameter> paramsToSet)
    {
        Map<String, AbstractCondition.LocalDynamicParameter> map = new HashMap<String, AbstractCondition.LocalDynamicParameter>();
        for (AbstractCondition.LocalDynamicParameter parameter : paramsToSet) {
            map.put(parameter.getName(), parameter);
        }
        return map;
    }
}
