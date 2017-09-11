package com.hit.groovy

import java.util.Map

import com.navis.apex.business.model.GroovyInjectionBase;
import com.navis.road.RoadEntity;
import com.navis.road.business.model.Truck;
import com.navis.road.business.model.TruckDriver;
import com.navis.road.business.model.TruckingCompany;

import com.navis.framework.util.BizViolation;
import com.navis.road.business.atoms.DriverStatusEnum;

import com.navis.framework.business.Roastery;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.Predicate;


public class HITGvyDriver extends GroovyInjectionBase{
	
	public String execute(Map inParams) {
		
		// Action declare
		String Action = (String) inParams.get("Action");
		String inCardId = (String) inParams.get("CardId");
		String inLicNbr = (String) inParams.get("License");
		String inStatus = (String) inParams.get("Status");				
		
		// Find Truck
		if(Action == "SEARCH") {
					
			if ((inCardId != "") || (inLicNbr != "")) {
				return findTruckDriver(inCardId, inLicNbr);
			}
			
		} else if (Action == "EXECUTE"){
				
			changeState(inCardId,inLicNbr,inStatus);
			
			String xmlResult = "<truckdriver>" +
			"<parameters>" +
			"<parameter name=\"BatNbr\" value=\"" + inCardId + "\"/>" +
			"<parameter name=\"TrkLicenseNbr\" value=\"" + inLicNbr + "\"/>" +
			"<parameter name=\"TrkStatus\" value=\"" + inStatus + "\"/>" +
			"</parameters>" +
			"</truckdriver>";
							
			return xmlResult;
            
		}		
		
	}
	//
	private String findTruckDriver(String inCardId, String inLicNbr) throws BizViolation {
						
				TruckDriver TrkEntityId = null;
				String xmlResult = null;
		
				try {
		
					if(inCardId != "") {		
						TrkEntityId = TruckDriver.findDriverByCardId(inCardId);		
					} else {		
						TrkEntityId = TruckDriver.findDriverByLicNbr(inLicNbr);		
					}
		
				} finally {
		
					if (TrkEntityId == null) {		
						return "No Truck Driver";		
					}
		
					Object driverName =  TrkEntityId.getDriverName();
					Object cardId = TrkEntityId.getDriverCardId();
					Object license = TrkEntityId.getDriverLicenseNbr();
					Object driverStatus = TrkEntityId.getDriverStatus();
													
					xmlResult = "<truckdriver>" +
							"<parameters>" +
							"<parameter name=\"CardId\" value=\"" + cardId + "\"/>" +
							"<parameter name=\"TrkLicenseNbr\" value=\"" + license + "\"/>" +
							"<parameter name=\"LastTruckDriver\" value=\"" + driverName + "\"/>" +							
							"<parameter name=\"TrkStatus\" value=\"" + driverStatus + "\"/>" +
							"</parameters>" +
							"</truckdriver>";
		
				}
		
				return xmlResult;
		
	}
	
	
	private void changeState(String inCardId, String inLicNbr,String inStatus) throws BizViolation
	{
		
		TruckDriver TrkEntityId = null;
		String xmlResult = null;

		if(inCardId != "") {
			TrkEntityId = TruckDriver.findDriverByCardId(inCardId);
		} else if(inLicNbr != ""){
			TrkEntityId = TruckDriver.findDriverByLicNbr(inLicNbr);
		}
				
		if(inStatus == "OK")
		{
			TrkEntityId.setDriverStatus(DriverStatusEnum.OK);
		}else if(inStatus == "BANNED")
		{
			TrkEntityId.setDriverStatus(DriverStatusEnum.BANNED);
		}
		
	}
	
	
	
	/*
	 * Sample
	 <groovy class-location="database" class-name="HITGvyDriver">
	 <parameters>
		<parameter id="Action" value="SEARCH"/>
		OR
		<parameter id="Action" value="EXECUTE"/>
		<parameter id="CardId" value="1069"/>
		<parameter id="License" value=""/>
		<parameter id="Status" value="BANNED"/>
		OR
		<parameter id="Status" value="OK"/>
	  </parameters>
	 </groovy>
	 */
	
	
}
