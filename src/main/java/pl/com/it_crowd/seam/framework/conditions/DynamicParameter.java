package pl.com.it_crowd.seam.framework.conditions;

/**
 * Dynamic condition parameter that should be evaluated on AbstractCondition.evaluate().
 */
public abstract class DynamicParameter {
// -------------------------- OTHER METHODS --------------------------

    public abstract Object getValue();
}
