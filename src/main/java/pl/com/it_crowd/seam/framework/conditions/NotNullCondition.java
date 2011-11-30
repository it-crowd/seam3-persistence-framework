package pl.com.it_crowd.seam.framework.conditions;

/**
 * Condition that is always rendered and is good for checking if value is not null.
 */
public class NotNullCondition extends AbstractNullabilityCondition {
// --------------------------- CONSTRUCTORS ---------------------------

    public NotNullCondition(Object arg)
    {
        super(arg);
    }

    @Override
    protected String getInstructionEJBQL()
    {
        return "IS NOT NULL";
    }
}
