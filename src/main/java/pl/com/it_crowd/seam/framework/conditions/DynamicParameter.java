package pl.com.it_crowd.seam.framework.conditions;

/**
 * Dynamic condition parameter that should be evaluated on AbstractCondition.evaluate().
 */
public interface DynamicParameter {
// -------------------------- OTHER METHODS --------------------------

    abstract Object getValue();
}
