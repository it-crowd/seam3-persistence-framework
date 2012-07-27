package pl.com.it_crowd.seam.framework.conditions;

public class FakeParameter<E> implements DynamicParameter<E> {
// ------------------------------ FIELDS ------------------------------

    private E value;

// --------------------------- CONSTRUCTORS ---------------------------

    public FakeParameter(E value)
    {
        this.value = value;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public E getValue()
    {
        return value;
    }

    public void setValue(E value)
    {
        this.value = value;
    }
}

