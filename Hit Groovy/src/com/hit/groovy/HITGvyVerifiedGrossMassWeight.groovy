package CodeExtension;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat
import java.util.Date;
import java.lang.StringBuffer;
import java.io.IOException;

import org.apache.log4j.Logger
import com.navis.argo.business.api.GroovyApi
import org.apache.log4j.Level
import com.navis.services.business.event.GroovyEvent


import com.navis.apex.business.model.GroovyInjectionBase;
import com.navis.argo.business.api.Serviceable;
import com.navis.argo.business.api.ServicesManager;
import com.navis.argo.business.atoms.UnitCategoryEnum;
import com.navis.framework.business.Roastery;
import com.navis.framework.metafields.Metafield;
import com.navis.framework.metafields.MetafieldIdFactory;
import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.persistence.Entity;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.framework.util.BizViolation;
import com.navis.argo.business.model.Complex;
import com.navis.argo.business.model.Operator;
import com.navis.argo.business.reference.Accessory;
import com.navis.argo.business.reference.Chassis;
import com.navis.external.framework.ui.EUIExtensionHelper;
import com.navis.inventory.business.units.UnitEquipment;
import com.navis.inventory.InventoryEntity;
import com.navis.inventory.InventoryField;
import com.navis.inventory.business.api.UnitField;
import com.navis.inventory.business.api.UnitFinder;
import com.navis.inventory.business.units.EqBaseOrder;
import com.navis.inventory.business.units.EqBaseOrderItem;
import com.navis.inventory.business.units.Unit;
import com.navis.inventory.business.units.UnitFacilityVisit;
import com.navis.inventory.business.units.UnitFacilityVisitHbr;
import com.navis.inventory.business.units.UnitFinderPea;
import com.navis.orders.BookingField;
import com.navis.orders.business.eqorders.Booking;
import com.navis.road.business.atoms.TruckStatusEnum;
import com.navis.road.business.model.Truck;
import com.navis.services.business.rules.EventType;
import com.navis.yard.rest.beans.Units;
import com.navis.framework.email.EmailMessage;
import com.navis.framework.email.EmailManager;

/**
 * @author gerson.tejeda
 *
 */
public class HITGvyVerifiedGrossMassWeight extends GroovyInjectionBase {

	/**
	 * @param inParams
	 */
	public String execute(Map inParams) {

		// Action declare
		String Action = (String) inParams.get("Action");

		// Find UnitNbr
		if(Action == "UNIT") {

			String UnitNbr = (String) inParams.get("UnitNbr");

			if(UnitNbr != "") {

				return findUnitNbrActiveExprt(UnitNbr);

			} else {

				return "Do not empty field!";

			}

		}

		// Find Truck
		if(Action == "TRUCK") {

			String BatNbr = (String) inParams.get("BatNbr");
			String ById = (String) inParams.get("ById");

			if ((BatNbr != "") || (ById != "")) {

				try {

					return findTruck(BatNbr, ById);

				} catch (BizViolation e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			} else {

				return "Do not empty field!";

			}

		}

		// Find Chassis
		if(Action == "CHASSIS") {

			String ChsId = (String) inParams.get("Chassis");

			if (ChsId != "") {

				try {

					return findChassis(ChsId);

				} catch (BizViolation e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			} else {

				return "Do not empty field!";

			}

		}

		// Find Accessory
		if(Action == "ACCESSORY") {

			String AcryId = (String) inParams.get("Accessory");

			if (AcryId != "") {

				try {

					return findAccessory(AcryId);

				} catch (BizViolation e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			} else {

				return "Do not empty field!";

			}

		}

		// Post VGM at UnitNbr active export
		if((Action == "POSTVGM") || (Action == "POSTVGM_NOTBILLABLE")) {

			String inUnitNbr = (String) inParams.get("UnitNbr");
			String inVGMWeight = (String) inParams.get("VGMWeight");
			String inVGMVerifier = (String) inParams.get("VGMVerifier");
			String inVGMVerified = (String) inParams.get("VGMVerified");
			String isBillable = Action == "POSTVGM" ? "SI" : "NO";

			if ((inUnitNbr != "") && (inVGMWeight != "") && (inVGMVerifier != "") && (inVGMVerified != "")) {

				return PostVgmToUnitNbrActiveExprt(inUnitNbr, inVGMWeight, inVGMVerifier, inVGMVerified,isBillable  );

			} else {

				return "Do not empty field!";

			}

		}

		return Action;

	}

	/**
	 * @param inUnitNbr
	 * @return
	 */
	private String findUnitNbrActiveExprt(String inUnitNbr) {

		/**
		 * UNIT
		 *
		 * Variable initialize
		 */ 
		String UnitNbr = inUnitNbr;
		UnitFacilityVisit UnitUfv = null;
		String xmlResult = null;
		Long UnitUfvGkey = null;
		Serializable UnitGkey = null;
		Object EqOiEqoNbr = null;

		// Find the export active UFV
		UnitUfv = findActiveUfv(UnitNbr);

		if (UnitUfv == null) {

			return "No Unit";

		}

		// Get UfvGkey from Unit
		UnitUfvGkey = UnitUfv.getUfvGkey();


		// Find the Unit gkey by active UFV gkey
		UnitGkey = findPrimaryKeysByDomainQuery(UnitNbr, UnitUfvGkey).toString().replace("[", "").replace("]", "");

		// Get Unit EntityId
		Entity EntityId = Unit.hydrate(UnitGkey);

		// Print Unit fields values entity
		Object UnitPrimaryEqTareWeightKg = EntityId.getFieldValue(UnitField.UNIT_PRIMARY_EQ_TARE_WEIGHT_KG);
		Object UnitEqIsoCode = EntityId.getFieldValue(UnitField.UNIT_EQ_ISO_CODE);
		Object UnitGdsShipperId = EntityId.getFieldValue(UnitField.UNIT_GDS_SHIPPER_ID);
		Object UnitGdsShipperName = EntityId.getFieldValue(UnitField.UNIT_GDS_SHIPPER_NAME);
		Object UnitGoodsAndCtrWtKg = EntityId.getFieldValue(UnitField.UNIT_GOODS_AND_CTR_WT_KG);
		Object UnitGoodsAndCtrWtKgVerifiedGross = EntityId.getFieldValue(InventoryField.UNIT_GOODS_AND_CTR_WT_KG_VERFIED_GROSS);
		Object UnitVgmEntity = EntityId.getFieldValue(InventoryField.UNIT_VGM_ENTITY);
		Object UnitVgmVerifiedDate = EntityId.getFieldValue(InventoryField.UNIT_VGM_VERIFIED_DATE);

		// findBooking

		EqOiEqoNbr = findBooking((Unit) EntityId);

		if (EqOiEqoNbr == null) {

			return "No Booking";
		}


		xmlResult = "<unitnbr-activeexprt>" +
				"<parameters>" +
				"<parameter name=\"UnitNbr\" value=\"" + UnitNbr + "\"/>" +
				/*
		 "<parameter name=\"UnitUfvGkey\" value=\"" + UnitUfvGkey + "\"/>" +
		 "<parameter name=\"UnitGkey\" value=\"" + UnitGkey + "\"/>" +
		 */
				"<parameter name=\"EqOiEqoNbr\" value=\"" + EqOiEqoNbr + "\"/>" +
				"<parameter name=\"UnitPrimaryEqTareWeightKg\" value=\"" + UnitPrimaryEqTareWeightKg + "\"/>" +
				"<parameter name=\"UnitEqIsoCode\" value=\"" + UnitEqIsoCode + "\"/>" +
				"<parameter name=\"UnitGdsShipperId\" value=\"" + UnitGdsShipperId + "\"/>" +
				"<parameter name=\"UnitGdsShipperName\" value=\"" + UnitGdsShipperName.toString().replaceAll("&", "").replaceAll("@", "").replaceAll("#", "") + "\"/>" +

				"<parameter name=\"UnitGoodsAndCtrWtKg\" value=\"" + UnitGoodsAndCtrWtKg + "\"/>" +
				"<parameter name=\"UnitGoodsAndCtrWtKgVerifiedGross\" value=\"" + UnitGoodsAndCtrWtKgVerifiedGross + "\"/>" +
				"<parameter name=\"UnitVgmEntity\" value=\"" + UnitVgmEntity + "\"/>" +
				"<parameter name=\"UnitVgmVerifiedDate\" value=\"" + UnitVgmVerifiedDate + "\"/>" +
				"</parameters>" +
				"</unitnbr-activeexprt>";


		return xmlResult;

	}

	/**
	 * @param inBatNbr
	 * @param inById
	 * @return
	 * @throws BizViolation
	 */
	private String findTruck(String inBatNbr, String inById) throws BizViolation {

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

				TrkEntityId = Truck.findTruckById(inById);

			}

		} finally {

			if (TrkEntityId == null) {

				return "No Truck";

			}

			Object TrkId = TrkEntityId.getTruckId();
			Object TrkLicenseNbr = TrkEntityId.getTruckLicenseNbr();
			Object TrkBatNbr = TrkEntityId.getTruckBatNbr();
			Object TrkTareWeightKg = TrkEntityId.getTruckTareWeight();
			Object TrkSafeWeightKg = TrkEntityId.getTruckSafeWeight();
			Object TrkStatus = TruckStatusEnum.OK;
			Object LastTruckingCompany = TrkEntityId.getTruckDriver();

			xmlResult = "<truck>" +
					"<parameters>" +
					"<parameter name=\"BatNbr\" value=\"" + TrkBatNbr + "\"/>" +
					"<parameter name=\"TrkId\" value=\"" + TrkId + "\"/>" +
					"<parameter name=\"TrkLicenseNbr\" value=\"" + TrkLicenseNbr + "\"/>" +
					"<parameter name=\"TrkTareWeightKg\" value=\"" + TrkTareWeightKg + "\"/>" +
					"<parameter name=\"TrkSafeWeightKg\" value=\"" + TrkSafeWeightKg + "\"/>" +
					"<parameter name=\"TrkStatus\" value=\"" + TrkStatus + "\"/>" +
					"<parameter name=\"LastTruckingCompany\" value=\"" + LastTruckingCompany + "\"/>" +
					"</parameters>" +
					"</truck>";

		}

		return xmlResult;

	}

	/**
	 * @param inChsId
	 * @return
	 * @throws BizViolation
	 */
	private String findChassis(String inChsId) throws BizViolation {

		/*
		 * CHASSI
		 * Variable initialize
		 */
		Chassis ChsEntityId = null;
		String xmlResult = null;

		try {

			ChsEntityId = Chassis.findChassis(inChsId);

		} finally {

			if (ChsEntityId == null) {

				return "No Chassis";

			}

			Object ChsEqTareWeightKg = ChsEntityId.getEqTareWeightKg();
			Object ChsEqSafeWeightKg = ChsEntityId.getEqSafeWeightKg();
			Object ChsEqEquipType = ChsEntityId.getEqEquipType();
			/*			
			 Object ChsEquipmentOwnerId = ChsEntityId.getEquipmentOwnerId();
			 Object ChsEquipmentOperatorId = ChsEntityId.getEquipmentOperatorId();
			 */

			xmlResult = "<chassis>" +
					"<parameters>" +
					"<parameter name=\"ChsId\" value=\"" + inChsId + "\"/>" +
					"<parameter name=\"ChsEqTareWeightKg\" value=\"" + ChsEqTareWeightKg + "\"/>" +
					"<parameter name=\"ChsEqSafeWeightKg\" value=\"" + ChsEqSafeWeightKg + "\"/>" +
					"<parameter name=\"ChsEqEquipType\" value=\"" + ChsEqEquipType + "\"/>" +
					"<parameter name=\"ChsEquipmentOwnerId\" value=\"" + "Hit" + "\"/>" +
					"<parameter name=\"ChsEquipmentOperatorId\" value=\"" + "Hit" + "\"/>" +
					"</parameters>" +
					"</chassis>";

		}

		return xmlResult;

	}

	/**
	 * @param inAcryId
	 * @return
	 * @throws BizViolation
	 */
	private String findAccessory(String inAcryId) throws BizViolation {

		/*
		 * GENSET
		 * Variable initialize
		 */
		Accessory AcryEntityId = null;
		String xmlResult = null;

		try {

			AcryEntityId = Accessory.findAccessory(inAcryId);

		} finally {

			if (AcryEntityId == null) {

				return "No Accessory";

			}

			Object AcryEqType = AcryEntityId.getEqEquipType();
			Object AcryEquipmentOwnerId = AcryEntityId.getEquipmentOwnerId();
			Object AcryEquipmentOperatorId = AcryEntityId.getEquipmentOperatorId();
			Object AcryEqTareWeightKg = AcryEntityId.getEqTareWeightKg();

			xmlResult = "<Accessory>" +
					"<parameters>" +
					"<parameter name=\"AcryId\" value=\"" + inAcryId + "\"/>" +
					"<parameter name=\"AcryEqType\" value=\"" + AcryEqType + "\"/>" +
					"<parameter name=\"AcryEquipmentOwnerId\" value=\"" + AcryEquipmentOwnerId + "\"/>" +
					"<parameter name=\"AcryEquipmentOperatorId\" value=\"" + AcryEquipmentOperatorId + "\"/>" +
					"<parameter name=\"AcryEqTareWeightKg\" value=\"" + AcryEqTareWeightKg + "\"/>" +
					"</parameters>" +
					"</Accessory>";

		}

		return xmlResult;

	}

	/**
	 * @param inUnitNbr
	 * @param inVGMWeight
	 * @param inVGMVerifier
	 * @param inVGMVerified
	 * @return
	 */
	private String PostVgmToUnitNbrActiveExprt(String inUnitNbr, String inVGMWeight, String inVGMVerifier, String inVGMVerified, String isBillable) throws BizViolation {

		/*
		 * POST-VGM
		 * 
		 * Variable initialize
		 */
		String UnitNbr = inUnitNbr;
		UnitFacilityVisit UnitUfv = null;
		Long UnitUfvGkey = null;
		Serializable UnitGkey = null;
		Entity EntityId = null;
		double VGMWeight = 0.00;
		String VGMVerifier = null;
		String VGMVerified = null;
		String xmlResult = null;

		// Find the export active UFV
		UnitUfv = findActiveUfv(UnitNbr);

		// Get UfvGkey from Unit
		UnitUfvGkey = UnitUfv.getUfvGkey();

		// Find the Unit gkey by active UFV gkey
		UnitGkey = findPrimaryKeysByDomainQuery(UnitNbr, UnitUfvGkey).toString().replace("[", "").replace("]", "");

		// Get Unit EntityId
		EntityId = Unit.hydrate(UnitGkey);

		// Update the Verified Gross Mass Weight
		VGMWeight = Double.parseDouble(inVGMWeight);
		VGMVerifier = inVGMVerifier;
		VGMVerified = inVGMVerified;

		// Posting value field
		EntityId.setFieldValue(InventoryField.UNIT_GOODS_AND_CTR_WT_KG_VERFIED_GROSS, VGMWeight);
		EntityId.setFieldValue(InventoryField.UNIT_VGM_ENTITY, VGMVerifier);
		EntityId.setFieldValue(InventoryField.UNIT_VGM_VERIFIED_DATE, VGMVerified);

		if ((VGMVerifier=="CE-HAINA TERMINAL") && (isBillable=="SI"))
		{
			postEvent('CERTIFICACION_VGM',EntityId,EntityId.unitId);

		}
		if ((VGMVerifier=="RE-HAINA TERMINAL") && (isBillable=="SI"))
		{
			postEvent('RECERTIFICACION_DEL_VGM',EntityId,EntityId.unitId);

		}

		//generear vermas
		generateVersmas(EntityId, inVGMWeight, inVGMVerifier,inVGMVerified);


		// Response value field
		xmlResult = "<post-vgm>" +
				"<parameters>" +
				"<parameter name=\"UnitNbr\" value=\"" + UnitNbr + "\"/>" +
				"<parameter name=\"VGMWeight\" value=\"" + VGMWeight + "\"/>" +
				"<parameter name=\"VGMVerifier\" value=\"" + VGMVerifier + "\"/>" +
				"<parameter name=\"VGMVerified\" value=\"" + VGMVerified + "\"/>" +
				"</parameters>" +
				"</post-vgm>";

		return xmlResult;

	}


	/**
	 * @param EntityId
	 */
	private void generateVersmas(Entity EntityId, String inVGMWeight, String inVGMVerifier, String inVGMVerified)
	{

		def date = new Date();
		def interchangeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		def interchange = interchangeFormat.format(date);
		//
		def formateDate = new SimpleDateFormat("yyyyMMdd");
		def formateTime = new SimpleDateFormat("hhmm");

		def yyyymmdd = formateDate.format(date);
		def hhmm = formateTime.format(date);


		//entity units
		def equipmentIdentification = EntityId.getField(UnitField.UNIT_ID);
		def codigoLinea = EntityId.getField(UnitField.UNIT_LINE_OPERATOR).toString().split(":");

		def linea ="";

		if(codigoLinea[1] == "46" || codigoLinea[1] == "6321" || codigoLinea[1] == "42")
		{
			def typeIso = EntityId.getField(UnitField.UNIT_EQ_ISO_CODE);
			def voyageID = EntityId.getField(UnitField.UNIT_CURRENT_UFV_ACTUAL_OB_CV);

			def grossWeight = EntityId.getField(UnitField.UNIT_GOODS_AND_CTR_WT_KG);
			//
			def EqOiEqoNbr = findBooking((Unit) EntityId);

			if (EqOiEqoNbr == null || EqOiEqoNbr =="") {
				EqOiEqoNbr ="No Booking";
			}
			//
			def bookingNumber = EqOiEqoNbr;

			def vgmVerified = inVGMVerified;
			def vgmVerifier = inVGMVerifier;
			def vgmWeightKg = inVGMWeight;

			//SBD
			if(codigoLinea[1] == "46" )
			{
				linea ="SBD";
			}
			//APL
			if(codigoLinea[1] == "42" )
			{
				linea ="APL";
			}
			//MSC
			if( codigoLinea[1] == "6321"){
				linea ="MSC";
			}


			def fileName = "vermas"+ interchange  +".edi";
			def fullPath = "//n4jobs/HIT_EDI/OUT/" + linea + "/VERMAS/"+ fileName;

			try {

				if(bookingNumber == "No Booking")
				{
					enviaEmail(equipmentIdentification, yyyymmdd,hhmm);
					return;
				}else{

					def oFile = new File(fullPath);
					oFile.createNewFile();
					def oFileWriter = new FileWriter(oFile);
					def bufferedWriter = new BufferedWriter(oFileWriter);
					def vermasBody = new StringBuffer();

					if(codigoLinea[1] == "46" )
					{
						vermasBody.append("UNB+UNOA:2+DOHAI+SMLU+" + yyyymmdd + ":" +  hhmm + "+" + interchange +"'\n");
					}else if(codigoLinea[1] == "42"){
						vermasBody.append("UNB+UNOA:2+DOHAI+" + linea + "+" + yyyymmdd + ":" +  hhmm + "+" + interchange +"'\n");
					}else if(codigoLinea[1] == "6321"){
						vermasBody.append("UNB+UNOA:2+DOHAI+" + linea + "+" + yyyymmdd + ":" +  hhmm + "+" + interchange +"'\n");
					}

					if(codigoLinea[1] == "46" )
					{
						vermasBody.append("UNH+"+ interchange  +"+VERMAS:D:16A:UN:SMDG04'\n");
					}else{
						vermasBody.append("UNH+VERMAS ID+VERMAS:D:16A:UN:SMDG04'\n");
					}

					vermasBody.append("BGM+XXX++9++39'\n");
					vermasBody.append("DTM+137:" + yyyymmdd + hhmm + ":203'\n");
					vermasBody.append("RFF+SI:T/HL007543'\n");
					vermasBody.append("DTM+171:"+ yyyymmdd + hhmm + ":203'\n");
					vermasBody.append("NAD+TR+DOHAI:TERMINALS:306'\n");

					if(codigoLinea[1] == "46" )
					{
						vermasBody.append("CTA+MS+SMLU'\n");
					}else if(codigoLinea[1] == "42" ) {
						vermasBody.append("CTA+MS+" + linea +"'\n");
					}else if(codigoLinea[1] == "6321" ) {
						vermasBody.append("CTA+MS+" + linea +"'\n");
					}

					vermasBody.append("COM+NAME(A)LINE.COM:EM'\n");
					vermasBody.append("EQD+CN+" + equipmentIdentification +":6346:5+" + typeIso + ":" + grossWeight +":5+++5'\n");
					vermasBody.append("RFF+BN:" + bookingNumber + "'\n");
					vermasBody.append("LOC+9+DOHAI+HIT:TERMINALS:306'\n");
					vermasBody.append("SEL+987654321+SH'\n");
					vermasBody.append("MEA+AAE+VGM+KGM:" + vgmWeightKg +"'\n");
					vermasBody.append("DTM+WAT:"+ yyyymmdd + hhmm + ":203'\n");
					vermasBody.append("TDT+20+123E45+++" + linea +":LINES:306+++9406960::11:GLUECKSBURSG'\n");
					vermasBody.append("RFF+VON:"+ voyageID + "'\n");

					if(vgmVerifier =="CE-HAINA TERMINAL" || vgmVerifier =="RE-HAINA TERMINAL" || vgmVerifier =="TER-HAINA TERMINAL"){
						vermasBody.append("DOC+SM1:VGM:306:WEIGHING CERTIFICATE+DOC-ID" + interchange + "'\n");
					}else{
						vermasBody.append("DOC+SHP:VGM:306+" +interchange +"'\n");
					}

					vermasBody.append("DTM+137:"+ yyyymmdd + hhmm +":203'\n");

					if(codigoLinea[1] == "46")
					{
						vermasBody.append("NAD+SPC+++HAINA INTERNATIONAL TERMINAL+Puerto Haina Oriental'\n");
					}else if(codigoLinea[1] == "42"){
						//vermasBody.append("NAD+SPC++++++++'\n");
					}else if(codigoLinea[1] == "6321"){
						vermasBody.append("NAD+SPC++++++++'\n");
					}

					vermasBody.append("CTA+RP+:" + vgmVerifier + "'\n");

					if(codigoLinea[1] == "46")
					{
						vermasBody.append("COM+Operaciones_HIT+'\n");
					}else if(codigoLinea[1] == "42"){
						//vermasBody.append("COM++'\n");
					}else if(codigoLinea[1] == "6321"){
						vermasBody.append("COM++'\n");
					}


					if(codigoLinea[1] == "46")
					{
						vermasBody.append("UNT+22+"+ interchange +"'\n");
					}else{
						vermasBody.append("UNT+16179+1'\n");
					}


					vermasBody.append("UNZ+1+" + interchange + "'\n");
					//vermas body

					bufferedWriter.write(vermasBody.toString());
					//
					bufferedWriter.flush();
					//
					bufferedWriter.close();
				}

			} catch (IOException e) {
				logInfo("ERRROR => generateVersmas " + e.getMessage());
			}catch (Exception e) {
				logInfo("ERRROR => generateVersmas " + e.getMessage());
			}
		}


	}
	/**
	 * @param inEquipmentId
	 * @param inUfvGkey
	 * @return
	 * @throws BizViolation
	 */
	private Serializable[] findPrimaryKeysByDomainQuery(String inEquipmentId, Object inUfvGkey) throws BizViolation {

		/**
		 * DomainQuery
		 * Variable initialize
		 */
		DomainQuery dq = null;

		try {

			dq = QueryUtils.createDomainQuery(InventoryEntity.UNIT)
					.addDqPredicate(PredicateFactory.eq(UnitField.UNIT_ACTIVE_UFV, inUfvGkey))
					.addDqPredicate(PredicateFactory.eq(UnitField.UNIT_ID, inEquipmentId))
					.addDqPredicate(PredicateFactory.eq(UnitField.UNIT_VISIT_STATE, "1ACTIVE"))
					.addDqPredicate(PredicateFactory.eq(UnitField.UNIT_CATEGORY, "EXPRT"));


		} finally {

		}

		return Roastery.getHibernateApi().findPrimaryKeysByDomainQuery(dq);

	}

	/**
	 * @param inUnitEntityId
	 * @return
	 * @throws BizViolation
	 */
	private String findBooking(Unit inUnitEntityId) throws BizViolation {

		/**
		 * BOOKING
		 * Variable initialize
		 */
		Unit UnitNbrEntityId = null;
		UnitEquipment UtEqRoutingPoint= null;
		EqBaseOrderItem EqBsOdImRoutingPoint = null;
		EqBaseOrder EqBsOdRoutingPoint = null;
		String EqBsOdNbr = null;
		Booking BookingNbr = null;
		List EqBsOdNbrList = null;

		try {

			UnitNbrEntityId = inUnitEntityId;
			UtEqRoutingPoint = UnitNbrEntityId.getUnitEquipment(UnitNbrEntityId.getUnitId());
			EqBsOdImRoutingPoint = UtEqRoutingPoint.getUeDepartureOrderItem();
			EqBsOdRoutingPoint = EqBsOdImRoutingPoint.getEqboiOrder();

		} finally {

			if (EqBsOdRoutingPoint != null) {

				// EqBsOdNbr is Equipment Base Order Number or Booking Number
				EqBsOdNbr = EqBsOdRoutingPoint.getEqboNbr();

				if (EqBsOdNbr == null) {

					return "No Booking";
				}
				EqBsOdNbrList = Booking.findBookingsByNbr(EqBsOdNbr);
				BookingNbr = EqBsOdNbrList[0];

			} else {

				return "Do not has a Booking Nbr!";
			}

		}

		return EqBsOdNbr.toString();

	}

	private void postEvent(String inEventId, Serviceable inServiceable, String inUnitId) {
		ServicesManager sm = (ServicesManager) Roastery.getBean(ServicesManager.BEAN_ID);
		EventType eventType = EventType.findEventType(inEventId);

		try
		{
			sm.recordEvent(eventType, null, null, null, inServiceable);
		}

		catch (BizViolation bv) {

		}

	}

	private void enviaEmail(String unitNbr,String fecha, String hora)
	{

		try{

			EmailMessage msg = new EmailMessage(ContextHelper.getThreadUserContext());
			msg.setTo("jose.encarnacion@hit.com.do");
			msg.setCc("mario.pimentel@hit.com.do");
			msg.setFrom("N4-Navis@hit.com");

			msg.setSubject("Alerta: Unidad sin Booking en el vermas");
			msg.setText("Unit Nbr : " + unitNbr + "Por favor verificar este caso. Fehca: " + fecha + ":" + hora +"");

			EmailManager manager = (EmailManager) Roastery.getBean("emailManager");
			manager.sendEmail(msg);


		}catch(BizViolation ex){

			log("ERROR enviaEmail : " + ex.getMessage());
		}


	}



}



/*
 * Sample
 <groovy class-location="database" class-name="HITGvyVerifiedGrossMassWeight">
 <parameters>
 <parameter id="Action" value="TRUCK"/>
 <parameter id="BatNbr" value="G525"/>	
 <parameter id="ById" value="AGML09"/>
 <parameter id="Action" value="ACCESSORY"/>
 <parameter id="Accessory" value="CMCG0005174"/>
 <parameter id="Action" value="CHASSIS"/>           
 <parameter id="Chassis" value="2001"/>
 <parameter id="Action" value="UNIT"/>
 <parameter id="UnitNbr" value="GETR2016912"/>
 <parameter id="Action" value="POSTVGM"/>
 <parameter id="UnitNbr" value="GATP1027531"/>           
 <parameter id="VGMWeight" value="35500"/>
 <parameter id="VGMVerifier" value="HAINA TERMINAL"/>
 <parameter id="VGMVerified" value="2016-05-26T22:31:30"/>
 </parameters>
 </groovy>
 */


