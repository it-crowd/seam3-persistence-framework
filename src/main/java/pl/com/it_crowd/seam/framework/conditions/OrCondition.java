package pl.com.it_crowd.seam.framework.conditions;

/**
 * Condition that renders non null arguments concatenated with OR condition
 */
public class OrCondition extends AbstractLogicalCondition {
// --------------------------- CONSTRUCTORS ---------------------------

    public OrCondition(Object... args)
    {
        super(args);
    }

    @Override
    protected String getOperator()
    {
        return " OR ";
    }
}
