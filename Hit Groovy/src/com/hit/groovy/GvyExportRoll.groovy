package com.hit.groovy

import com.navis.inventory.business.units.Unit;
import com.navis.argo.business.api.GroovyApi;
import com.navis.services.business.event.GroovyEvent;


public class GvyExportRoll extends GroovyApi {
		
	public void execute(GroovyEvent event, Object api){
		
		Unit unit = (Unit)event.getEntity();
		
		String flexString10 = unit.getUnitFlexString10();
		String flexString04 = unit.getUnitFlexString04();
		
		unit.updateRemarks(flexString10 + flexString04);
		println("Unit Remarks:" + flexString10 + " " + flexString04);
			
	}
	
}
