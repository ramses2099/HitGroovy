package com.hit.groovy

import com.navis.billing.business.model.Invoice;
import com.navis.external.framework.ECallingContext;
import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor;
import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;
import com.navis.framework.portal.FieldChanges;

public class HITGvyGetInvoiceItem extends AbstractEntityLifecycleInterceptor
{

	//EEntityView inEntity, EFieldChangesView inOriginalFieldChanges, EFieldChanges inMoreFieldChanges
    @Override
	public void onCreate(EEntityView inEntity, EFieldChangesView inOriginalFieldChanges, EFieldChanges inMoreFieldChanges) {
		Invoice factura  = inEntity._entity;
		String rs = factura.getInvoiceCurrency().getCurrencySymbol();
		log("HITGvyGetInvoiceItem : " + rs);
	}

	@Override
    public void log(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}
	
	
 }

