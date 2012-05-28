package pl.com.it_crowd.seam.framework;

import junit.framework.Assert;
import org.junit.Test;
import pl.com.it_crowd.seam.framework.conditions.AbstractCondition;
import pl.com.it_crowd.seam.framework.conditions.DynamicParameter;
import pl.com.it_crowd.seam.framework.conditions.FakeParameter;
import pl.com.it_crowd.seam.framework.conditions.FreeCondition;
import pl.com.it_crowd.seam.framework.conditions.OrCondition;
import pl.com.it_crowd.seam.framework.mocks.ElContextMock;
import pl.com.it_crowd.seam.framework.mocks.ExpressionFactoryMock;

import javax.el.ValueExpression;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynParamEntityQueryTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void allParamsNonEmpty()
    {
        CustomQuery customQuery = new CustomQuery();
        customQuery.setConditions(Arrays.asList(new FreeCondition("a=", new NotNullParam(), "b=", new NotNullParam())));
        customQuery.isAnyConditionParameterDirty();
        QueryMock query = new QueryMock();
        customQuery.setParameters(query);
        Assert.assertEquals(2, query.params.size());
    }

    @Test
    public void oneParamOfFreeConditionEmpty()
    {
        CustomQuery customQuery = new CustomQuery();
        customQuery.setConditions(Arrays.asList(new FreeCondition("a=", new NullParam(), "b=", new NotNullParam())));
        customQuery.isAnyConditionParameterDirty();
        QueryMock query = new QueryMock();
        customQuery.setParameters(query);
        Assert.assertEquals(0, query.params.size());
    }

    @Test
    public void refreshOnDynamicParameterOnCollectionChange()
    {
        final List<Integer> list = new ArrayList<Integer>();
        final FakeParameter<List<Integer>> param = new FakeParameter<List<Integer>>(list);
        final CustomQuery query = new CustomQuery();
        FreeCondition condition = new FreeCondition("u.id in (", param, ")");
        query.setConditions(Arrays.asList(condition));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());

        list.addAll(Arrays.asList(1, 2, 3));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
        list.addAll(Arrays.asList(4, 5, 6));
        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
    }

    @Test
    public void refreshOnDynamicParameterOnCollectionChangeInCompositeCondition()
    {
        final List<Integer> list = new ArrayList<Integer>();
        final FakeParameter<List<Integer>> param = new FakeParameter<List<Integer>>(list);
        final CustomQuery query = new CustomQuery();
        AbstractCondition condition = new OrCondition(new FreeCondition("u.id in (", param, ")"), new FreeCondition("false"));
        query.setConditions(Arrays.asList(condition));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());

        list.addAll(Arrays.asList(1, 2, 3));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
        list.addAll(Arrays.asList(4, 5, 6));
        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
    }

    @Test
    public void refreshOnDynamicParameterOnPrimitiveBooleanChangeInCompositeCondition()
    {
        final FakeParameter<Object> param = new FakeParameter<Object>(true);
        final CustomQuery query = new CustomQuery();
        AbstractCondition condition = new OrCondition(new FreeCondition("u.active  = ", param), new FreeCondition("false"));
        query.setConditions(Arrays.asList(condition));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());

        param.setValue(false);

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
    }

    @Test
    public void refreshOnDynamicParameterOnStringChange()
    {
        final FakeParameter<String> param = new FakeParameter<String>("Jan");
        final CustomQuery query = new CustomQuery();
        FreeCondition condition = new FreeCondition("u.id in (", param, ")");
        query.setConditions(Arrays.asList(condition));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());

        param.setValue("Bono");

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
        param.setValue("Tony");
        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
    }

    @Test
    public void refreshOnDynamicParameterOnStringChangeInCompositeCondition()
    {
        final FakeParameter<String> param = new FakeParameter<String>("Jan");
        final CustomQuery query = new CustomQuery();
        AbstractCondition condition = new OrCondition(new FreeCondition("u.id in (", param, ")"), new FreeCondition("false"));
        query.setConditions(Arrays.asList(condition));

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());

        param.setValue("Bono");

        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
        param.setValue("Tony");
        Assert.assertTrue("query parameters should be dirty", query.isAnyParameterDirty());
        query.getResultList();
        Assert.assertFalse("query parameters should be clean", query.isAnyParameterDirty());
    }

// -------------------------- INNER CLASSES --------------------------

    private class CustomQuery extends DynParamEntityQuery {
// --------------------------- CONSTRUCTORS ---------------------------

        private CustomQuery()
        {
            setEjbql("select u from User u");
            try {
                Field expressions = pl.com.it_crowd.seam.framework.Query.class.getDeclaredField("expressions");
                expressions.setAccessible(true);
                Instance<MyExpressions> myExpressionses = new Instance<MyExpressions>() {
                    @Override
                    public Instance<MyExpressions> select(Annotation... qualifiers)
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public boolean isUnsatisfied()
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public boolean isAmbiguous()
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public <U extends MyExpressions> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers)
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public <U extends MyExpressions> Instance<U> select(Class<U> subtype, Annotation... qualifiers)
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Iterator<MyExpressions> iterator()
                    {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public MyExpressions get()
                    {
                        return new MyExpressions(new ElContextMock(), new ExpressionFactoryMock()) {
                            @Override
                            public ValueExpression createValueExpression(String expression)
                            {
                                return null;
                            }
                        };
                    }
                };
                expressions.set(this, myExpressionses);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

// -------------------------- OTHER METHODS --------------------------

        @Override
        public void setParameters(Query query)
        {
            super.setParameters(query);
        }

        @Override
        protected Query createQuery()
        {
            parseEjbql();

            evaluateAllParameters();

            Query query = new QueryMock() {
                @SuppressWarnings("unchecked")
                @Override
                public List getResultList()
                {
                    return new ArrayList(params.values());
                }
            };
            setParameters(query);
            return query;
        }
    }

    private class NotNullParam implements DynamicParameter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface DynamicParameter ---------------------

        @Override
        public Object getValue()
        {
            return "value";
        }
    }

    private class NullParam implements DynamicParameter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface DynamicParameter ---------------------

        @Override
        public Object getValue()
        {
            return null;
        }
    }

    private class QueryMock implements Query {
// ------------------------------ FIELDS ------------------------------

        protected Map<String, Object> params = new HashMap<String, Object>();

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Query ---------------------

        public List getResultList()
        {
            throw new UnsupportedOperationException();
        }

        public Object getSingleResult()
        {
            throw new UnsupportedOperationException();
        }

        public int executeUpdate()
        {
            throw new UnsupportedOperationException();
        }

        public Query setMaxResults(int maxResult)
        {
            throw new UnsupportedOperationException();
        }

        public int getMaxResults()
        {
            throw new UnsupportedOperationException();
        }

        public Query setFirstResult(int startPosition)
        {
            throw new UnsupportedOperationException();
        }

        public int getFirstResult()
        {
            throw new UnsupportedOperationException();
        }

        public Query setHint(String hintName, Object value)
        {
            throw new UnsupportedOperationException();
        }

        public Map<String, Object> getHints()
        {
            throw new UnsupportedOperationException();
        }

        public <T> Query setParameter(Parameter<T> param, T value)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(String name, Object value)
        {
            params.put(name, value);
            return this;
        }

        public Query setParameter(String name, Calendar value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(String name, Date value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(int position, Object value)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(int position, Calendar value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Query setParameter(int position, Date value, TemporalType temporalType)
        {
            throw new UnsupportedOperationException();
        }

        public Set<Parameter<?>> getParameters()
        {
            throw new UnsupportedOperationException();
        }

        public Parameter<?> getParameter(String name)
        {
            throw new UnsupportedOperationException();
        }

        public <T> Parameter<T> getParameter(String name, Class<T> type)
        {
            throw new UnsupportedOperationException();
        }

        public Parameter<?> getParameter(int position)
        {
            throw new UnsupportedOperationException();
        }

        public <T> Parameter<T> getParameter(int position, Class<T> type)
        {
            throw new UnsupportedOperationException();
        }

        public boolean isBound(Parameter<?> param)
        {
            throw new UnsupportedOperationException();
        }

        public <T> T getParameterValue(Parameter<T> param)
        {
            throw new UnsupportedOperationException();
        }

        public Object getParameterValue(String name)
        {
            throw new UnsupportedOperationException();
        }

        public Object getParameterValue(int position)
        {
            throw new UnsupportedOperationException();
        }

        public Query setFlushMode(FlushModeType flushMode)
        {
            throw new UnsupportedOperationException();
        }

        public FlushModeType getFlushMode()
        {
            throw new UnsupportedOperationException();
        }

        public Query setLockMode(LockModeType lockMode)
        {
            throw new UnsupportedOperationException();
        }

        public LockModeType getLockMode()
        {
            throw new UnsupportedOperationException();
        }

        public <T> T unwrap(Class<T> cls)
        {
            throw new UnsupportedOperationException();
        }
    }
}
