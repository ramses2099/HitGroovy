package com.hit.groovy

import java.util.List;
import com.navis.argo.ArgoExtractEntity;
import com.navis.argo.ArgoExtractField;
import com.navis.framework.business.Roastery;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.inventory.InventoryEntity
import com.navis.inventory.InventoryField;
import com.navis.inventory.business.units.Unit


/**
 * @author jose.encarnacion
 * @date 03-11-2016
 */
public class HITGvyDomainQuery {
	
	
	/*--
	public List execute(){
		
		DomainQuery dq = QueryUtils.createDomainQuery(InventoryEntity.UNIT).
						  addDqPredicate(PredicateFactory.eq(InventoryField.UNIT_GKEY ,"3002120"));
		
	   return Roastery.getHibernateApi().findEntitiesByDomainQuery(dq);
		
	}
	--*/
	
	
	public void execute(){
		
		DomainQuery dq = QueryUtils.createDomainQuery(InventoryEntity.UNIT).
						  addDqPredicate(PredicateFactory.eq(InventoryField.UNIT_GKEY ,"3002120"));
		
	    Unit unit = (Unit)Roastery.getHibernateApi().findEntitiesByDomainQuery(dq)[0];
		
		println("UnitNbr :" + unit.getUnitCategory());
		
	}
			
		
}
