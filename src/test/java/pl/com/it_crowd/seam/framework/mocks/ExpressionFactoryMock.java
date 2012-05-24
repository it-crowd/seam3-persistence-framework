package pl.com.it_crowd.seam.framework.mocks;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

public class ExpressionFactoryMock extends ExpressionFactory {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public Object coerceToType(Object obj, Class targetType)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MethodExpression createMethodExpression(ELContext context, String expression, Class expectedReturnType, Class[] expectedParamTypes)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression createValueExpression(Object instance, Class expectedType)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression createValueExpression(ELContext context, String expression, Class expectedType)
    {
        throw new UnsupportedOperationException();
    }
}
