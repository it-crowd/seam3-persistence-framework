package pl.com.it_crowd.seam.framework.conditions;

import java.io.Serializable;

/**
 * Dynamic condition parameter that should be evaluated on AbstractCondition.evaluate().
 */
public interface DynamicParameter<E> extends Serializable {
// -------------------------- OTHER METHODS --------------------------

    abstract E getValue();
}
