package com.hit.groovy


import com.navis.framework.business.Roastery;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.billing.business.model.Invoice;
import com.navis.billing.BillingEntity;
import com.navis.billing.BillingField;


/**
 * @author jose.encarnacion
 * @date 08/01/2018
 * @procedure groovy for extract info the invoices
 */
public class HITGvyGetInvoice {

	public String execute() {
		
		//INVOICE_STATUS
		
		//DomainQuery dq = QueryUtils.createDomainQuery(BillingEntity.INVOICE).
		//addDqPredicate(PredicateFactory.eq(BillingField.INVOICE_GKEY  ,"883858"));
		
		DomainQuery dq = QueryUtils.createDomainQuery(BillingEntity.INVOICE).
		addDqPredicate(PredicateFactory.eq(BillingField.INVOICE_STATUS  ,"Final"));
		
		
		Invoice invoice = (Invoice)Roastery.getHibernateApi().findEntitiesByDomainQuery(dq)[0];
		
		println("Invoice :" + invoice.getInvoiceDraftNbr());
		
		return "Hello World!"
	}
	
}
