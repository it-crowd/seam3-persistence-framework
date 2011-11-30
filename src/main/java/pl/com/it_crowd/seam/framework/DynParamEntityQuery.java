package pl.com.it_crowd.seam.framework;

import pl.com.it_crowd.seam.framework.conditions.AbstractCondition;

import javax.persistence.Query;
import java.util.List;

public class DynParamEntityQuery<E> extends EntityQuery<E> {
// ------------------------------ FIELDS ------------------------------

    private List<AbstractCondition> conditions;

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<AbstractCondition> getConditions()
    {
        return conditions;
    }

    public void setConditions(List<AbstractCondition> conditions)
    {
        this.conditions = conditions;
    }

    @Override
    protected void appendRestrictionsEjbql(StringBuilder builder)
    {
        super.appendRestrictionsEjbql(builder);
        if (conditions != null) {
            for (AbstractCondition condition : conditions) {
                String ejbqlPart = condition.getRenderedEJBQL();
                if (ejbqlPart != null && !"".equals(ejbqlPart.trim())) {
                    if (WHERE_PATTERN.matcher(builder).find()) {
                        builder.append(" ").append(getRestrictionLogicOperator()).append(" ");
                    } else {
                        builder.append(" where ");
                    }
                    builder.append(ejbqlPart);
                }
            }
        }
    }

    @Override
    protected Query createCountQuery()
    {
        Query countQuery = super.createCountQuery();
        setParameters(countQuery);
        return countQuery;
    }

    @Override
    protected Query createQuery()
    {
        Query query = super.createQuery();
        setParameters(query);
        return query;
    }

    @Override
    protected void evaluateAllParameters()
    {
        super.evaluateAllParameters();
        int paramOffset = getRestrictionParameterValues().size() + getQueryParameterValues().size() + 1;
        if (conditions != null) {
            for (AbstractCondition condition : conditions) {
                condition.setParamIndexOffset(paramOffset);
                condition.evaluate();
                paramOffset += condition.getDynamicParametersCount();
            }
        }
    }

    private boolean isAnyConditionParameterDirty()
    {
        if (conditions != null) {
            for (AbstractCondition condition : conditions) {
                condition.evaluate();
                if (condition.isDirty()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isAnyParameterDirty()
    {
        return super.isAnyParameterDirty() || isAnyConditionParameterDirty();
    }

    private void setParameters(Query query)
    {
        if (conditions != null) {
            for (AbstractCondition condition : conditions) {
                for (AbstractCondition.LocalDynamicParameter parameter : condition.getParamsToSet()) {
                    query.setParameter(parameter.getName(), parameter.getValue());
                }
            }
        }
    }
}
