package com.hit.groovy

import com.navis.billing.business.api.IInvoiceManager;
import com.navis.billing.business.model.Invoice;
import com.navis.billing.business.api.IInvoiceManager;
import com.navis.framework.business.Roastery;
import com.navis.billing.BillingField;
import com.navis.billing.business.atoms.InvoiceStatusEnum;
import java.text.SimpleDateFormat;
import com.navis.billing.business.model.InvoiceHbr;

public class HITGvyFinalizarFacturaAlmacenaje {

    public String execute(Map inParameters) {


	String invoiceGkeyT = (String)inParameters.get("INVOICEKEY");

	Long invoiceGkey = Long.parseLong(invoiceGkeyT);

	def fecha = new Date();

	Invoice factura = Invoice.hydrate(invoiceGkey);

	factura.finalizeInvoice(fecha);

        	return ;

    }
}