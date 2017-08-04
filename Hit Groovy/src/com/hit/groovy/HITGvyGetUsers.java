package com.hit.groovy;


import com.navis.framework.portal.UserContext;
import com.navis.argo.ContextHelper;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.security.SecurityField;
import com.navis.framework.portal.QueryUtils;
import com.navis.argo.business.security.ArgoUser;
import com.navis.framework.portal.UserContext;
import com.navis.argo.ContextHelper;
import com.navis.framework.business.Roastery;


public class HITGvyGetUsers {

    public String execute() {

        String userName = "null";
        ///////// Busca Usuario //////
       UserContext userContext = ContextHelper.getThreadUserContext();
        userName =userContext.getUserId();
       return userName;
}
	
	
}
