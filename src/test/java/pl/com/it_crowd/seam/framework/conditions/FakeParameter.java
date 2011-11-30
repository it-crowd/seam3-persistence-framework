package pl.com.it_crowd.seam.framework.conditions;

public class FakeParameter extends DynamicParameter {
// ------------------------------ FIELDS ------------------------------

    private Object value;

// --------------------------- CONSTRUCTORS ---------------------------

    public FakeParameter(Object value)
    {
        this.value = value;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}

