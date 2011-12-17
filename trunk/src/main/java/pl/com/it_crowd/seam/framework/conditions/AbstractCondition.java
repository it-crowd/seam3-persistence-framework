package pl.com.it_crowd.seam.framework.conditions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract query condition.
 * Method #evaluate() calculates dynamic parameters' values and prepares renderedEJBQL. Even if values of dynamic parameters change they will not be taken
 * under consideration until next #evaluate() call.
 * Before calling #getRenderedEJBQL() call #evaluate().
 */
public abstract class AbstractCondition {
// ------------------------------ FIELDS ------------------------------

    protected Object[] argValues;

    protected Object[] args;

    protected Set<LocalDynamicParameter> dynamicParams = new HashSet<LocalDynamicParameter>();

    protected String oldEJBQL;

    protected int paramIndexOffset = 1;

    protected String paramPrefix = "qel";

// --------------------------- CONSTRUCTORS ---------------------------

    public AbstractCondition(Object... args)
    {
        this.args = args == null ? new Object[0] : args;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Gets offset of parameter index. If we have some param already in query i.e.: :qel1,:qel2 then params used in this condition should start from :qel3.
     *
     * @return offset of parm index
     */
    public int getParamIndexOffset()
    {
        return paramIndexOffset;
    }

    public void setParamIndexOffset(int paramIndexOffset)
    {
        this.paramIndexOffset = paramIndexOffset;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Calculate values of dynamic parameters and prepares rendered EJBQL fragment.
     */
    public void evaluate()
    {
        oldEJBQL = getRenderedEJBQL();
        evaluateArgumentValues();
        renderEJBQL();
    }

    public int getDynamicParametersCount()
    {
        int paramCount = 0;
        for (Object o : args) {
            if (o instanceof DynamicParameter) {
                paramCount++;
            } else if (o instanceof AbstractCondition) {
                paramCount += ((AbstractCondition) o).getDynamicParametersCount();
            }
        }
        return paramCount;
    }

    /**
     * Gets collection of parameters that will be in EJBQL fragment and should be set on Query object.
     *
     * @return set of parameters that need to be set on query
     */
    public Set<LocalDynamicParameter> getParamsToSet()
    {
        final HashSet<LocalDynamicParameter> set = new HashSet<LocalDynamicParameter>();
        for (LocalDynamicParameter parameter : dynamicParams) {
            if (parameter.value != null && (!(parameter.value instanceof Collection) || !((Collection) parameter.value).isEmpty())) {
                set.add(parameter);
            }
        }
        for (Object o : args) {
            if (o instanceof AbstractCondition) {
                set.addAll(((AbstractCondition) o).getParamsToSet());
            }
        }
        return set;
    }

    /**
     * Gets EJBQL fragment representing this condition.
     *
     * @return EJBQL fragment representing this condition.
     */
    public abstract String getRenderedEJBQL();

    public boolean isDirty()
    {
        String renderedEJBQL = getRenderedEJBQL();
        if (oldEJBQL == null && renderedEJBQL != null || oldEJBQL != null && renderedEJBQL == null || oldEJBQL != null && !oldEJBQL.equals(renderedEJBQL)) {
            return true;
        }
        for (AbstractCondition.LocalDynamicParameter parameter : getParamsToSet()) {
            if (parameter.isDirty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells if condition has any EJBQL to render.
     *
     * @return true if yes; false if nothing will be rendered
     */
    public boolean rendersEJBQL()
    {
        String renderedEJBQL = getRenderedEJBQL();
        return renderedEJBQL != null && !"".equals(renderedEJBQL.trim());
    }

    private void evaluateArgumentValues()
    {
        dynamicParams.clear();
        argValues = new Object[args.length];
        int localDynP = 0;
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            if (o instanceof DynamicParameter) {
                LocalDynamicParameter localDynamicParameter = new LocalDynamicParameter((DynamicParameter) o, paramPrefix + (localDynP++ + paramIndexOffset),
                    ((DynamicParameter) o).getValue());
                argValues[i] = localDynamicParameter;
                dynamicParams.add(localDynamicParameter);
            } else {
                if (o instanceof AbstractCondition) {
                    final AbstractCondition condition = (AbstractCondition) o;
                    condition.paramIndexOffset = localDynP + paramIndexOffset;
                    condition.evaluate();
                    localDynP += condition.getDynamicParametersCount();
                }
                argValues[i] = o;
            }
        }
    }

    /**
     * Generates EJBQL fragment representig this condition.
     */
    protected abstract void renderEJBQL();

    /**
     * Gets string representation of object that should be used in EJBQL part.
     *
     * @param o object to convert to EJBQL part representation
     *
     * @return query parameter name if o is instance of LocalDynamicParameter, renderedEJBQL fragment if o is instance of AbstractCondition, toString if o is not null or null if o is null
     */
    protected String toEJBQLPart(Object o)
    {
        if (o instanceof LocalDynamicParameter) {
            Object value = ((LocalDynamicParameter) o).value;
            return value == null || (value instanceof Collection && ((Collection) value).isEmpty()) ? null : ":" + ((LocalDynamicParameter) o).name;
        } else if (o instanceof AbstractCondition) {
            ((AbstractCondition) o).renderEJBQL();
            return ((AbstractCondition) o).getRenderedEJBQL();
        } else if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }

// -------------------------- INNER CLASSES --------------------------

    public class LocalDynamicParameter {
// ------------------------------ FIELDS ------------------------------

        DynamicParameter dynamicParameter;

        String name;

        Object value;

// --------------------------- CONSTRUCTORS ---------------------------

        private LocalDynamicParameter(DynamicParameter dynamicParameter, String name, Object value)
        {
            this.dynamicParameter = dynamicParameter;
            this.name = name;
            this.value = value;
        }

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }

// -------------------------- OTHER METHODS --------------------------

        public boolean isDirty()
        {
            Object freshValue = this.dynamicParameter.getValue();
            return value == null ? (freshValue != null && (!(freshValue instanceof Collection) || !((Collection) freshValue).isEmpty())) : value.equals(
                freshValue);
        }
    }

    protected class OffsetHolder {
// ------------------------------ FIELDS ------------------------------

        int offset;

// --------------------------- CONSTRUCTORS ---------------------------

        public OffsetHolder(int offset)
        {
            this.offset = offset;
        }
    }
}
