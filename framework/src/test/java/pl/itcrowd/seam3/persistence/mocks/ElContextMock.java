package pl.itcrowd.seam3.persistence.mocks;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class ElContextMock extends ELContext {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public ELResolver getELResolver()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public FunctionMapper getFunctionMapper()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public VariableMapper getVariableMapper()
    {
        throw new UnsupportedOperationException();
    }
}
