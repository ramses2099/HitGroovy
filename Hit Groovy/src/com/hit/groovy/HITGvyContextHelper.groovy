package com.hit.groovy


import com.navis.framework.portal.UserContext;
import com.navis.argo.ContextHelper;


public class HITGvyContextHelper {

	
	public String execute(){
		
		def out = ""
		def context = ContextHelper.getThreadUserContext()
		
		out = context.getUserId() + " " + context.getUserLocale() + " " + context.getTimeZone() + "" 
		
		return out;
	}
	
}
