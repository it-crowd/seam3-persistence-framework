package pl.com.it_crowd.seam.framework;

import junit.framework.Assert;
import org.junit.Test;
import pl.com.it_crowd.seam.framework.conditions.DynamicParameter;
import pl.com.it_crowd.seam.framework.conditions.FreeCondition;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

// -------------------------- INNER CLASSES --------------------------

    private class CustomQuery extends DynParamEntityQuery {
// --------------------------- CONSTRUCTORS ---------------------------

        private CustomQuery()
        {
            setEjbql("select u from User u");
        }

// -------------------------- OTHER METHODS --------------------------

        @Override
        public void setParameters(Query query)
        {
            super.setParameters(query);
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

        private Map<String, Object> params = new HashMap<String, Object>();

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
