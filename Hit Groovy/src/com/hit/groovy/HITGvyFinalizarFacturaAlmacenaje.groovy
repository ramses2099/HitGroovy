package com.hit.groovy

import com.navis.billing.business.api.IInvoiceManager;
import com.navis.billing.business.model.Invoice;
import com.navis.billing.business.model.InvoiceType;
import com.navis.billing.business.model.InvoiceItem;
import com.navis.billing.business.api.IInvoiceManager;
import com.navis.framework.business.Roastery
import com.navis.argo.business.api.GroovyApi
import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType;
import com.navis.billing.BillingField;
import com.navis.billing.business.atoms.InvoiceStatusEnum;
import java.text.SimpleDateFormat;
import com.navis.billing.business.model.InvoiceHbr;
import com.navis.framework.persistence.DatabaseHelper;

import java.util.List;
import java.util.regex.Pattern;

import java.util.Map
import java.util.regex.Pattern;
import javax.xml.rpc.Stub;
import javax.sql.RowSet;

import com.navis.framework.business.Roastery
import com.navis.argo.ContextHelper
import com.navis.argo.business.api.GroovyApi
import com.navis.argo.business.reference.Equipment
import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType;
import com.navis.services.business.rules.EventType
import com.navis.framework.portal.query.DomainQuery
import com.navis.framework.portal.QueryUtils
import com.navis.services.business.event.Event
import com.navis.services.ServicesEntity
import com.navis.services.ServicesField
import com.navis.www.services.argoservice.ArgoServiceLocator
import com.navis.www.services.argoservice.ArgoServicePort
import com.navis.framework.persistence.HibernateApi
import com.navis.framework.portal.query.PredicateFactory
import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.persistence.HibernatingEntity;
import com.navis.framework.metafields.MetafieldIdFactory;

import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType;
import com.navis.argo.webservice.types.v1_0.ResponseType;
import com.navis.argo.webservice.types.v1_0.ScopeCoordinateIdsWsType;
import com.navis.services.business.event.GroovyEvent;


public class HITGvyFinalizarFacturaAlmacenaje extends GroovyApi{

	//web service navistest
	//private static final String ARGO_SERVICE_URL ="http://172.16.0.94:9080/apex/services/argoservice";
	
	//web service navistest
	private static final String ARGO_SERVICE_URL ="http://loadbalancer/apex/services/argoservice";

	private DatabaseHelper _dbHelper;
	private String query1;
		
    public String execute(Map inParameters) {

		_dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);
			
		String invoiceGkeyT = (String)inParameters.get("INVOICEKEY");
				
		Long invoiceGkey = Long.parseLong(invoiceGkeyT);
	
		def fecha = new Date();
	
		Invoice factura = Invoice.hydrate(invoiceGkey);
	
		InvoiceType invoiceType = factura.getInvoiceInvoiceType()
		
		Long invtypeGkey = invoiceType.getInvtypeGkey();
		
		factura.finalizeInvoice(fecha);
		
		if(invtypeGkey== 3 || invtypeGkey == 87 || invtypeGkey ==88 || invtypeGkey == 89){
			query1 = "exec CLUSTERNAVIDB.apex.dbo.relaseInvoicesItemAlmacenaje @draft_nbr =" + factura.getInvoiceDraftNbr() + ";";
			
			log("INFO HITGvyFinalizarFacturaAlmacenaje :" + query1);
			
			List lstItemAlmacenaje = _dbHelper.queryForList(query1);
		
		}else{
		
			List<InvoiceItem> listInvoiceItems =  InvoiceItem.getInvoiceItemByInvoice(factura);
		
			if(listInvoiceItems.size() > 0){
				
				for(InvoiceItem item : listInvoiceItems){
				
					String UnitNbr = item.getItemEventEntityId();
				
					String  xmlQuery = "<hpu>"+
								"<entities>" +
								"<units>" +
								"<unit-identity id=\""+ UnitNbr + "\" type=\"CONTAINERIZED\"/>" +
								"</units>" +
								"</entities>" +
								"<flags>" +
								"<flag hold-perm-id=\"FACTURACION_PERMISO\" action=\"RELEASE_HOLD\" note=\"Permiso removido por HITGvyFinalizarFacturaAlmacenaje \"/>" +
								"</flags>" +
								"</hpu>";
						
					logInfo("INFO HITGvyFinalizarFacturaAlmacenaje : " + xmlQuery);
					
					//CALL WEB SERVICE ARGO
					GenericInvokeResponseWsType response;
					ResponseType commonResponse;
					String webserviceResponse;
							 
					response = callGenericWebservice(xmlQuery);
					commonResponse = response.getCommonResponse();
				}
			}
		}
		
	    return;

    }
	//
	public static GenericInvokeResponseWsType callGenericWebservice(String inQueryXML) throws Exception {

		GenericInvokeResponseWsType response = null;
		ScopeCoordinateIdsWsType scope = new ScopeCoordinateIdsWsType();
		scope.setOperatorId("HIT");
		scope.setComplexId("SANTO_DOMINGO");
		scope.setFacilityId("HAINA_TERMINAL");
		scope.setYardId("HITYRD");
		// Identify the Web Services host
		ArgoServiceLocator service = new ArgoServiceLocator();
		ArgoServicePort port = service.getArgoServicePort(new URL(ARGO_SERVICE_URL));
		Stub stub = (Stub) port;
		// Specify the User ID and the Password
		stub._setProperty(Stub.USERNAME_PROPERTY, "admin");
		stub._setProperty(Stub.PASSWORD_PROPERTY, "Navistest");
		response = port.genericInvoke(scope, inQueryXML);
		return response;
	}
	//
	@Override
	public void log(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}
		
	
}