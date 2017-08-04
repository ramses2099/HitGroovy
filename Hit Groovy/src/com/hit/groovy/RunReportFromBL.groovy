import com.navis.argo.ArgoExtractEntity;
import com.navis.argo.ArgoExtractField;
import com.navis.argo.ArgoField;
import com.navis.argo.ArgoRefEntity;
import com.navis.argo.ArgoRefField;
import com.navis.argo.ContextHelper;

import com.navis.argo.presentation.reports.DownloadManager;

import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType;
import com.navis.argo.webservice.types.v1_0.ResponseType;
import com.navis.argo.webservice.types.v1_0.ScopeCoordinateIdsWsType;
import com.navis.cargo.CargoLotFields;
import com.navis.cargo.InventoryCargoField;
import com.navis.cargo.business.model.BillOfLading;
import com.navis.cargo.business.model.CargoLot;
import com.navis.cargo.web.CargoGuiMetafield;
import com.navis.external.framework.AbstractExtensionCallback;
import com.navis.external.framework.EExtensionCallback;
import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.request.AbstractSimpleRequest;
import com.navis.external.framework.request.ESimpleReadRequest;
import com.navis.external.framework.ui.AbstractTableViewCommand;
import com.navis.framework.portal.KeyValuePair;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.UserContext;
import com.navis.framework.portal.context.UserContextUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.framework.ulc.server.application.view.ViewHelper;
import com.navis.www.services.argoservice.ArgoServiceLocator;
import com.navis.www.services.argoservice.ArgoServicePort;
import com.navis.external.framework.ui.EUIExtensionHelper;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.framework.business.Roastery;
import com.navis.framework.metafields.MetafieldId;
import com.navis.framework.metafields.entity.EntityId;
import com.navis.framework.persistence.Entity;
import com.navis.framework.util.message.MessageLevel;
import com.navis.services.business.event.GroovyEvent;
import com.navis.external.framework.ui.command.AbstractUiCommandProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.Stub;

import com.navis.framework.util.TransactionParms

/*
 * Mod by Rob Rojas, to generate report in Servlet environment from stored report definitions
 * */

class GetReportWebserviceClient extends AbstractTableViewCommand{


			private static final String ARGO_SERVICE_URL ="http://172.16.0.94:9080/apex/services/argoservice";
			private static String ERRORS = "3";

			public void execute(EntityId inEntityId, List<Serializable> inGkeys,Map<String, Object> inParams) {

				        TransactionParms tp = TransactionParms.getBoundParms();
				        tp.scopingEnabled = true;				

				String BL = buscarBLPorPK(inEntityId.getEntityName(),inGkeys[0]);
				
				
				//log("Entity :" + inEntityId.getEntityName());

				/*					
				EUIExtensionHelper extHelper = getExtensionHelper();
				String dialogTitle = "Testing table view command extension";
				extHelper.showMessageDialog(MessageLevel.INFO, dialogTitle, "Hello! " + tp);
				*/
				
	
				  String xmlQuery = "<report-def>" +
						 "<get-report-url report-name=\"CREHIT\">" +
						 "<parameters>" +
						 "<parameter name=\"BL\"" +
						 " value=\"" + BL + "\"/>" +
						 "</parameters>" +
					   	 "</get-report-url>" +
						 "</report-def>"; 
				 
			DownloadManager myDownMan = new DownloadManager();
			GenericInvokeResponseWsType response = callGenericWebservice(xmlQuery);
			ResponseType commonResponse = response.getCommonResponse();
			if (commonResponse.getStatus().equals(ERRORS)) {
			System.err.println("Web service returned error:\n" + commonResponse.getStatusDescription());
			}

			// getting report URL with webservice response
			String webserviceResponse = commonResponse.getQueryResults(0).getResult();
			String reportUrl = webserviceResponse.substring(webserviceResponse.indexOf('>') + 1, webserviceResponse.lastIndexOf('<'));
			String reportID = webserviceResponse.substring(webserviceResponse.indexOf('=') + 1, webserviceResponse.lastIndexOf('<'));

		
			myDownMan.showDocument(reportID);
			
			}
			
/**
* Calls generic web service
* @param inQueryXML
* @return
* @throws Exception
*/
			
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
	stub._setProperty(Stub.PASSWORD_PROPERTY, "Navistest*");
	response = port.genericInvoke(scope, inQueryXML);
	return response;
	}

private Serializable buscarBLPorPK(String inEntityName, Serializable inPrimaryKey) {

	  MetafieldId field = InventoryCargoField.BL_NBR;
	  Serializable BLPk = (Serializable) ViewHelper.getEntityFieldValue(inEntityName, inPrimaryKey, field);
	  
    return BLPk;
  }


}