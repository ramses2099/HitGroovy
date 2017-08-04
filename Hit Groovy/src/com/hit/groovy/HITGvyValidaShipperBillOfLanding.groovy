package com.hit.groovy

import com.navis.cargo.CargoBizMetafield;
import com.navis.cargo.InventoryCargoField
import com.navis.cargo.business.model.BillOfLading;
import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor
import com.navis.external.framework.entity.EEntityView
import com.navis.external.framework.util.EFieldChange;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;
import com.navis.framework.util.BizViolation;

public class HITGvyValidaShipperBillOfLanding extends AbstractEntityLifecycleInterceptor {

	@Override
	public void onUpdate(EEntityView inEntity, EFieldChangesView inOrginalFielChanges, EFieldChanges inMoreFielChanges) {
		
			try{				
				
				//BillOfLading billOfLading = inEntity._entity;
				
				String shipper = (String)inEntity.getField(CargoBizMetafield.BL_SHIPPER_AS_STRING);
				//  Double itemAmount = (Double) inEntity.getField(BillingField.ITEM_AMOUNT);
														
				log("INFO HITGvyValidaShipperBillOfLanding : " + shipper);
								
				if(shipper == "--"){
					log("ERROR HITGvyValidaShipperBillOfLanding : el shipper name esta en blanco");
				}
					
			}catch(BizViolation ex){
				
				log("ERROR HITGvyValidaShipperBillOfLanding : " + ex.getMessage());
			}
		
	}

	@Override
	public void log(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}
	
}
