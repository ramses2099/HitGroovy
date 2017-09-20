package com.hit.groovy;

import java.util.Map
import java.util.regex.Pattern;
import javax.xml.rpc.Stub;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import com.navis.inventory.business.api.UnitFinder
import com.navis.apex.business.model.GroovyInjectionBase;
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
import com.navis.inventory.business.units.Unit
import com.navis.framework.portal.query.PredicateFactory
import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.persistence.HibernatingEntity;
import com.navis.framework.metafields.MetafieldIdFactory;

import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType;
import com.navis.argo.webservice.types.v1_0.ResponseType;
import com.navis.argo.webservice.types.v1_0.ScopeCoordinateIdsWsType;
import com.navis.services.business.event.GroovyEvent;


public class HITGvyPermissionPCT extends GroovyInjectionBase {

	//web service navistest
	private static final String ARGO_SERVICE_URL ="http://172.16.0.94:9080/apex/services/argoservice";
	
	//web service produccion
	//private static final String ARGO_SERVICE_URL ="http://172.16.0.53:9080/apex/services/argoservice";

	
	private DatabaseHelper _dbHelper;
	
	public static String OK = "0";
	public static String INFO = "1";
	public static String WARNINGS = "2";
	public static String ERRORS = "3";
	

	public String execute(GroovyEvent event) {
		
		String UnitNbr = event.getProperty('UnitNbr');		
		
		logInfo("[HITGvyPermissionDPH]: " + UnitNbr);
		
		if(UnitNbr != null && !UnitNbr.isEmpty()){
		
			_dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);
				
			String query = "SELECT TOP 1 [Action],[Nota] FROM [HIT_SQL01].[N4EDI].[dbo].[TransaccionWebApiHPUPCT] WHERE [UnitNbr] ='" + UnitNbr +"' AND Estatus ='PENDIENTE' ORDER BY Id DESC";
			
			logInfo("[HITGvyPermissionPCT]: " + query);
	
			List Lista = _dbHelper.queryForList(query);
	
			String Action ="";
			String Note ="";
			
			String xmlQuery = "";
			
			if(Lista.size() > 0){
				
				String[] rows = Lista.toString().replace("[", "").replace("]", "").split(Pattern.quote(","));
				   
				if(rows.length > 0){
					   Action = rows[0].split(Pattern.quote(":"))[1]; 
					   Note = rows[1].split(Pattern.quote(":"))[1];			   
				
					   //			
					   xmlQuery = "<hpu>"+
								"<entities>" +
								"<units>" +
								"<unit-identity id=\""+ UnitNbr + "\" type=\"CONTAINERIZED\"/>" +
								"</units>" +
								"</entities>" +
								"<flags>" +
								"<flag hold-perm-id=\"PCT_PERMISO\" action=\"" + Action + "\" note=\"" + Note +"\"/>" +
								"</flags>" +
								"</hpu>";
						
					   logInfo("[HITGvyPermissionDPH]: " + xmlQuery);
								
					   //CALL WEB SERVICE ARGO		
					   GenericInvokeResponseWsType response;
					   ResponseType commonResponse;
					   String webserviceResponse;
								
					   response = callGenericWebservice(xmlQuery);
					   commonResponse = response.getCommonResponse();
					   
					   String sql = "UPDATE [HIT_SQL01].[N4EDI].[dbo].[TransaccionWebApiHPUPCT] SET [Estatus] = 'COMPLETADO',[FechaActualizacion] = GETDATE() WHERE Id = (SELECT MAX(Id) FROM [HIT_SQL01].[N4EDI].[dbo].[TransaccionWebApiHPUDPH] WHERE [UnitNbr] ='" + UnitNbr + "') AND Estatus ='PENDIENTE';";
					   									
					   logInfo("[HITGvyPermissionDPH]: " + sql);
																		
					   if (commonResponse.getStatus().equals(ERRORS)) {
						   logInfo("[HITGvyPermissionPCT]: Web service returned error: " + commonResponse.getStatusDescription());					   	 
					   }else{ 						  
						   _dbHelper.jtUpdate(sql);
					   }
					   				   
				}		
			 }		

		}
		return "";

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
		//stub._setProperty(Stub.USERNAME_PROPERTY, "admin");
		stub._setProperty(Stub.USERNAME_PROPERTY, "pct.webapi");		
		stub._setProperty(Stub.PASSWORD_PROPERTY, "abcd.1234");
		response = port.genericInvoke(scope, inQueryXML);
		return response;
	}
	
	@Override
	public void logInfo(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}


	
	
}