package pl.com.it_crowd.seam.framework.converter;

import javax.faces.view.facelets.ConverterConfig;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.persistence.EntityManager;

public class EntityConverterHandler extends ConverterHandler {
// ------------------------------ FIELDS ------------------------------

    private final TagAttribute entityManager;

    private final TagAttribute nullEntity;

    private final TagAttribute transientEntity;

// --------------------------- CONSTRUCTORS ---------------------------

    public EntityConverterHandler(ConverterConfig config)
    {
        super(config);
        this.entityManager = this.getAttribute("entityManager");
        this.transientEntity = this.getAttribute("transientEntity");
        this.nullEntity = this.getAttribute("nullEntity");
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public MetaRuleset createMetaRuleset(Class type)
    {
        return super.createMetaRuleset(type).ignoreAll();
    }

    @Override
    public void setAttributes(FaceletContext ctx, Object instance)
    {
        EntityConverter converter = (EntityConverter) instance;
        if (this.nullEntity != null) {
            converter.setNullEntity(this.nullEntity.getValue(ctx));
        }
        if (this.transientEntity != null) {
            converter.setTransientEntity(this.transientEntity.getValue(ctx));
        }
        if (this.entityManager != null) {
            converter.setEntityManager((EntityManager) this.entityManager.getObject(ctx));
        }
    }
}
