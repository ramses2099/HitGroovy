/*
* Sample Groovy Class.  As below, your class must have a no-arg execute() method that returns a String.
*/

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

public class GroovyHello extends GroovyInjectionBase{

 private static final Logger LOGGER = Logger.getLogger(GroovyHello.class);
 private static final String ENTITYNAME = "ArgoExtractEntity.CHARGEABLE_UNIT_EVENT";

 public String execute(Map inParams) {


   DomainQuery dq = QueryFactory.createDomainQuery(ArgoExtractEntity.CHARGEABLE_UNIT_EVENT);
   dq.addDqPredicate(PredicateFactory.eq(ArgoExtractField.BEXU_STATUS, "QUEUED"));
   dq.addDqOrdering(Ordering.asc(ArgoExtractField.BEXU_EQ_ID));
   dq.addDqOrdering(Ordering.asc(ArgoExtractField.BEXU_EVENT_TYPE));
   
   List <Event> elementList = HibernateApi.getInstance().findEntitiesByDomainQuery(dq);
  
   factura.getField(MetafieldIdFactory.valueOf("customFlexFields.invoiceCustomDFFBLS")).toString();
   
   
   log("Groovy GroovyHello: Hello World!");
   
   return "Hello World!";
  }
}