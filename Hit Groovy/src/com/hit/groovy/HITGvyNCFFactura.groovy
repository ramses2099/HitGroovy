package com.hit.groovy

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import groovy.util.slurpersupport.NodeChildren;
import wslite.soap.*
import java.util.Map;


import com.navis.billing.business.model.Invoice;
import com.navis.billing.business.model.InvoiceType;
import com.navis.argo.business.model.ArgoSequence;
import com.navis.billing.BillingField;


class HITGvyNCFFactura {

	public String execute(Map inParams) {

		//def invoiceType = (String) inParams.get("invoiceType");
		def draftNbr = (String) inParams.get("draftNbr");

		//def draftNbr = "930197"

		String tipoSecuencia = "";

		Invoice inv = Invoice.findInvoiceByDraftNbr(draftNbr)

		def invTypeId = inv.getInvoiceInvoiceType().toString().split(":")[1]
		def invType = InvoiceType.hydrate(invTypeId)

		def invSeq = invType.getInvtypeSeqGkey()

		switch(invSeq) {
			case 5://NCF_FINAL
				tipoSecuencia = "1"
				break
			case 6://NCF_FISCAL
				tipoSecuencia = "2"
				break
			case 7://NCF_ESPECIAL
				tipoSecuencia = "3"
				break
			case 8://NCF_GUBERNAMENTAL
				tipoSecuencia = "4"
				break
			case 3://NOTA_CREDITO
				tipoSecuencia = "5"
				break
			default ://NOTA_CREDITO
				tipoSecuencia = "1"
				break
		}


		def ncf = getSeccuenciaNCF(tipoSecuencia,draftNbr)
		
		inv.setFieldValue(BillingField.INVOICE_FINAL_NBR, ncf)

		return ncf;
	}


	private String getSeccuenciaNCF(String TipoSecuencia, String NumeroFactura) {

		def Sistema = 1

		def client = new SOAPClient('http://hit-app02/WebServiceNCF/WebServiceNCF.asmx')
		def response = client.send( """<?xml version="1.0" encoding="utf-8"?>
											<soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
											  <soap12:Body>
											    <GetNCF xmlns="http://tempuri.org/">
											      <param>
											        <TipoSecuencia>$TipoSecuencia</TipoSecuencia>
											        <Sistema>$Sistema</Sistema>
											        <NumeroFactura>$NumeroFactura</NumeroFactura>
											      </param>
											    </GetNCF>
											  </soap12:Body>
											</soap12:Envelope>""")


		def	rs = response.GetNCFResponse.GetNCFResult.NCF;

		return rs;
	}

	/*--
	 * <groovy class-name="HITGvyNCFFactura" class-location="database">
	 <parameters>
	 <parameter id="draftNbr" value="930204"/>
	 </parameters>
	 </groovy>
	 --*/


}

