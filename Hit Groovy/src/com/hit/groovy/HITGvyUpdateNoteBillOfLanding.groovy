package com.hit.groovy

import com.navis.cargo.business.model.BillOfLading
import com.navis.cargo.InventoryCargoField;
import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor
import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;

public class HITGvyUpdateNoteBillOfLanding extends AbstractEntityLifecycleInterceptor{

	@Override
	public void onUpdate(EEntityView inEntityView, EFieldChangesView inOriginalFieldChanges,
		 EFieldChanges inMoreFieldChanges) {
		
		inMoreFieldChanges.setFieldChange(InventoryCargoField.BL_NOTES,"BL cambiado por CE: HITGvyUpdateNoteBillOfLanding");		
	 
			
	}
	
	
}
