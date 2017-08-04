package com.hit.groovy

import org.apache.log4j.Logger
import org.apache.log4j.Priority;

import com.navis.argo.business.api.GroovyApi
import com.navis.framework.ulc.server.application.util.logging.Log4jLogger;
import com.navis.services.business.event.GroovyEvent

public class GvyLogInfo extends GroovyApi{

	private static final Logger LOGGER = Logger.getLogger(GvyLogInfo.class);
	
	public void execute(Map inParams){
		
		logWarn("GvyLogInfo: Este no funciona 2");
	 	System.err.println("GvyLogInfo: Este da error");	
	}
		
}


/*---
import org.apache.log4j.Logger
import com.navis.argo.business.api.GroovyApi
import com.navis.framework.ulc.server.application.util.logging.Log4jLogger;
import com.navis.services.business.event.GroovyEvent

public class GvyLogInfo extends GroovyApi{

	private static final Logger LOGGER = Logger.getLogger(GvyLogInfo.class);
	
	public void execute(GroovyEvent event, Object api){
		
		logInfo("GvyLogInfo: Este no funciona");
		logWarn("GvyLogInfo: Prueba");
		log("GvyLogInfo: Este si funciona");
		LOGGER.log(Priority.INFO, "GvyLogInfo: logger info");
		
		//registerWarning("GvyLogInfo: registrando un warning");
		//registerError("GvyLogInfo: registrando un errror");
		 
		log("GvyLogInfo apex log: map=$inParams");

		//System.out.println("GvyLogInfo println: Este si funciona 6");
		//System.err.println("GvyLogInfo  println: Este da error");	
				
	}
	
	
}
--*/