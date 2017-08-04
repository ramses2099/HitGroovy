import com.navis.apex.business.model.GroovyInjectionBase;
import com.navis.extension.model.persistence.DynamicHibernatingEntity;
import com.navis.framework.metafields.MetafieldIdFactory;
import com.navis.framework.persistence.HibernateApi;
import com.navis.framework.portal.Ordering;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.QueryFactory;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.navis.services.ServicesEntity;
import com.navis.services.ServicesField;
import com.navis.framework.portal.QueryUtils;
import com.navis.services.business.event.Event;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.services.business.rules.EventType;
import com.navis.framework.metafields.Metafield;
import com.navis.argo.ArgoExtractEntity;
import com.navis.argo.ArgoExtractField;
import com.navis.argo.ArgoField;

class GroovyHello {
    public String execute() {

        DomainQuery dq = QueryFactory.createDomainQuery("CustomServicio");
        dq.addDqPredicate(PredicateFactory.eq(MetafieldIdFactory.valueOf("customFlexFields.customservicGLCode"), "041301040204"));
        dq.addDqOrdering(Ordering.asc(MetafieldIdFactory.valueOf("customFlexFields.customservicPrecio")));
        dq.addDqOrdering(Ordering.asc(MetafieldIdFactory.valueOf("customFlexFields.customservicIdFamilia")));
   
        HibernateApi.getInstance().findEntitiesByDomainQuery(dq);

        return "Hello World!"
    }
}