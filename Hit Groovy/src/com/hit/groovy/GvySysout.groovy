package com.hit.groovy

import org.apache.log4j.Logger
import org.apache.log4j.Level
import com.navis.apex.business.model.GroovyInjectionBase
import com.navis.argo.business.api.GroovyApi
import com.navis.services.business.event.GroovyEvent

public class GvySysout extends GroovyInjectionBase{

	public void execute(GroovyEvent event, GroovyApi api){
		String UnitNbr = (String)event.getProperty("UnitNbr");
		log("Groovy GvySysout: UnitNbr: " + UnitNbr);
		api.logWarn("Groovy GvySysout TEST: UnitNbr: " + UnitNbr);
	}	
	
}
