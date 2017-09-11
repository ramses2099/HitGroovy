package com.hit.groovy

import java.util.Map

import com.navis.apex.business.model.GroovyInjectionBase;
import com.navis.road.RoadEntity;
import com.navis.road.business.model.Truck;
import com.navis.road.business.model.TruckDriver;
import com.navis.road.business.model.TruckingCompany;

import com.navis.framework.util.BizViolation;
import com.navis.road.business.atoms.TruckStatusEnum;

import com.navis.framework.business.Roastery;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.Predicate;



public class HITGvyTruck extends GroovyInjectionBase {
	
	
	public String execute(Map inParams) {
				
		// Action declare
		String Action = (String) inParams.get("Action");
		String inBatNbr = (String) inParams.get("BATNumber");
		String inLicNbr = (String) inParams.get("License");
		String inStatus = (String) inParams.get("Status");

		
		// Find Truck
		if(Action == "SEARCH") {
					
			if ((inBatNbr != "") || (inLicNbr != "")) {

				return findTruck(inBatNbr, inLicNbr);

		    }
			
		} else if (Action == "EXECUTE"){
				
			changeState(inBatNbr,inLicNbr,inStatus);
			
			String xmlResult = "<truck>" +
			"<parameters>" +
			"<parameter name=\"BatNbr\" value=\"" + inBatNbr + "\"/>" +
			"<parameter name=\"TrkLicenseNbr\" value=\"" + inLicNbr + "\"/>" +
			"<parameter name=\"TrkStatus\" value=\"" + inStatus + "\"/>" +
			"</parameters>" +
			"</truck>";
							
			return xmlResult;


		}
	
		
	}

	

	private String findTruck(String inBatNbr, String inLicNbr) throws BizViolation {

		/*
		 * TRUCK
		 * Variable initialize
		 */
		Truck TrkEntityId = null;
		String xmlResult = null;

		try {

			if(inBatNbr != "") {

				TrkEntityId = Truck.findTruckByBatNbr(inBatNbr);

			} else {

				TrkEntityId = Truck.findTruckByLicNbr(inLicNbr);

			}

		} finally {

			if (TrkEntityId == null) {

				return "No Truck";

			}

			Object TrkId = TrkEntityId.getTruckId();
			Object TrkLicenseNbr = TrkEntityId.getTruckLicenseNbr();
			Object TrkBatNbr = TrkEntityId.getTruckBatNbr();
			Object TrkStatus = TrkEntityId.getTruckStatus();
			
			//Object[] lastTruckingCompany = TrkEntityId.getTruckTrkCo().toString().split(":");
			Object[] lastTruckDriver = TrkEntityId.getTruckDriver().toString().split(":");
            
			Object driverName ="";
			//Object driverLicense ="";
			
			if(lastTruckDriver[1] != null) 
			{
					
				DomainQuery dq = QueryUtils.createDomainQuery(RoadEntity.TRUCK_DRIVER)
				.addDqPredicate(Predicate.eq("driverGkey",lastTruckDriver[1]));
		
				TruckDriver truckDriver = (TruckDriver)Roastery.getHibernateApi().getUniqueEntityByDomainQuery(dq);

				driverName = truckDriver.getDriverName();
								
			}
									
			
			xmlResult = "<truck>" +
					"<parameters>" +
					"<parameter name=\"BatNbr\" value=\"" + TrkBatNbr + "\"/>" +
					"<parameter name=\"TrkLicenseNbr\" value=\"" + TrkLicenseNbr + "\"/>" +
					"<parameter name=\"TrkStatus\" value=\"" + TrkStatus + "\"/>" +
					"<parameter name=\"LastTruckDriver\" value=\"" + driverName + "\"/>" +
					"</parameters>" +
					"</truck>";

		}

		return xmlResult;

	}

	private void changeState(String inBatNbr, String inLicNbr,String inStatus) throws BizViolation 
	{
		
		Truck TrkEntityId = null;
		
		if(inBatNbr != "") {
			TrkEntityId = Truck.findTruckByBatNbr(inBatNbr);
		} else {
			TrkEntityId = Truck.findTruckByLicNbr(inLicNbr);
		}
		
		if(inStatus == "OK") 
		{
			TrkEntityId.setTruckStatus(TruckStatusEnum.OK);
		}else if(inStatus == "BANNED")
		{
			TrkEntityId.setTruckStatus(TruckStatusEnum.BANNED);
		}
		
	}
			
	
	
	/*
	 * Sample
	 <groovy class-location="database" class-name="HITGvyTruck">
	 <parameters>
	    <parameter id="Action" value="SEARCH"/>
	    OR
	    <parameter id="Action" value="EXECUTE"/>	 
	    <parameter id="BATNumber" value="R063"/>
	    <parameter id="License" value="L217627"/>
	    <parameter id="Status" value="BANNED"/>
	    OR
	    <parameter id="Status" value="OK"/>	    
	  </parameters>
	 </groovy>
	 */
		
	
	
}
