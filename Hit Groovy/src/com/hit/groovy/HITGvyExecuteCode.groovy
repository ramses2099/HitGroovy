package com.hit.groovy

import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.UserContext
import com.navis.framework.portal.query.DomainQuery
import com.navis.argo.ArgoExtractEntity;
import com.navis.argo.ContextHelper;
import com.navis.inventory.business.units.Unit;  

class HITGvyExecuteCode {
	
	public String execute(){
		/*--
		UserContext userContext = ContextHelper.getThreadUserContext();
		String userInfo = "User Id :" + userContext.getUserId() + ", User Locate :" + userContext.getUserLocale() + ", Time Zone :" + userContext.getTimeZone() + "";		
		return userInfo;
		--*/
	
		DomainQuery dq = QueryUtils.createDomainQuery(ArgoExtractEntity.CHARGEABLE_UNIT_EVENT);  
			
	}
	
}
