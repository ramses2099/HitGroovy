package com.hit.groovy


import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor;
import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;
import com.navis.framework.util.BizViolation;
import com.navis.orders.business.eqorders.EquipmentOrder;
import com.navis.orders.business.eqorders.EquipmentOrderItem;
import com.navis.inventory.business.units.EqBaseOrder;
import com.navis.argo.business.reference.EquipType;
import com.navis.framework.metafields.Metafield;
import com.navis.framework.metafields.MetafieldIdFactory;

public class HITGvyCreateBookingItem extends AbstractEntityLifecycleInterceptor{
	
		
	@Override
	public void onUpdate(EEntityView inEntityView, EFieldChangesView inOriginalFieldChanges, EFieldChanges inMoreFieldChanges) {
				
		try{
			
			EqBaseOrder eqBase = (EqBaseOrder)inEntityView._entity;
			//
			EquipType  eqType = EquipType.findEquipType("2080");
						
			EquipmentOrderItem.createOrderItem(eqBase, 10L, eqType); 
				
		}catch(BizViolation ex){
			
			log("ERROR HITGvyCreateBookingItem : " + ex.getMessage());
		}
					
	}
	//	

	@Override
	public void log(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}
	//
	
	
}
