package CodeExtension;

import java.util.List;

import com.navis.cargo.business.model.BillOfLading;
import com.navis.cargo.business.model.BlGoodsBl;
import com.navis.cargo.business.model.GoodsBl;
import com.navis.inventory.business.units.Unit;
import com.navis.cargo.business.model.InventoryCargoManagerPea;
import com.navis.edi.business.edimodel.EdiPostManagerPea;
import com.navis.external.edi.entity.AbstractEdiPostInterceptor;
import com.navis.external.edi.entity.EEdiPostInterceptor;
import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.persistence.HibernatingEntity;
import com.navis.framework.metafields.MetafieldIdFactory;
import com.navis.framework.business.Roastery;
import java.util.Map;
import com.navis.framework.util.ValueObject;
import java.util.LinkedHashMap;

/**
 * 
 * @author gersontejeda
 * EDI Post Interceptor to change DGA Family code.....
 * Insert Familia Mercancia Descripcion  by blCustomDFFFamiliaMercancia.....
 *
 */
public class EdiPostInterceptorManifest extends AbstractEdiPostInterceptor
{
	private DatabaseHelper _dbHelper;
	
	public void afterEdiPost(org.apache.xmlbeans.XmlObject inXmlTransactionDocument, HibernatingEntity inHibernatingEntity, Map inParams)
	{
		BillOfLading Bol = (BillOfLading) inHibernatingEntity;
		String Familia = Bol.getBlNotifyParty1Address1();
		String Units;
		
		if (Familia != null)
		{	
			String DescFamilia = getDescription(Familia);
			int len = getUnits(Bol).length();
			
			//validacion campo unidad logintud 2000
			if(len >= 2000){
				Units = getUnits(Bol).substring(0,1999);
			}else{
				Units = getUnits(Bol);
			}

			
			Bol.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.blCustomDFFFamiliaMercancia"), Familia);
			Bol.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.blCustomDFF_FMDESCRIPCION"), DescFamilia);
			Bol.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.blCustomDFFUNIDADES"), Units);
		}
		//
		//clearUnitsSeals(Bol);
	}
	
	/**
	 * @param CodigoFlia
	 * @return
	 */
	private String getDescription(String CodigoFlia)
	{
		_dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);
		String query = "SELECT CUSTOMFAMILI_DESCRIPCION FROM HIT_SQL01.BILLING.dbo.CUSTOM_FAMILIA_MERCANCIA WHERE CUSTOMFAMILI_IDFAMILIA = " + CodigoFlia;
		
		List Lista = _dbHelper.queryForList(query);
		
		return Lista.toString().replace("[", "").replace("]", "").replace("CUSTOMFAMILI_DESCRIPCION:", "");
		
/*		ValueObject valor;
		ValueObject[] arregloValores = new ValueObject[Lista.size()];
		LinkedHashMap mapa = new LinkedHashMap();
		
		for (Object pk : Lista)
		{
			if (pk.CUSTOMFAMILI_DESCRIPCION != null)
			{
				valor = new ValueObject(pk.CUSTOMFAMILI_DESCRIPCION);
				arregloValores[0] = valor;
				mapa.put(pk.CUSTOMFAMILI_DESCRIPCION, String.valueOf(pk.CUSTOMFAMILI_IDFAMILIA));
			}
		}*/
		
		//return mapa.toString().replace("[", "").replace("]", "");
		//return Lista.get(0);
	}
	
	private String getUnits(BillOfLading BillOfLadingNbr)
	{
		Unit Unit;
	    GoodsBl goodsBl;
	    String Unidades = "";
	    
	    Set<BlGoodsBl> blGoodsBlSet = BillOfLadingNbr.getBlBlGoodsBls();

	    if (blGoodsBlSet != null) {
	    	for (BlGoodsBl blGoodsBl : blGoodsBlSet)
	        {
	    		goodsBl = blGoodsBl.getBlgdsblGoodsBl();
	            Unit = goodsBl.getGdsUnit();
	                        
	            Unidades += Unit.getUnitId() !=null ? Unit.getUnitId() + ",":"";

	            //SELLOS MANIFESTADOS
	            /*--
				String UnitSealNbr1 = Unit.getUnitSealNbr1();
	            String UnitSealNbr2 = Unit.getUnitSealNbr2();
	            String UnitSealNbr3 = Unit.getUnitSealNbr3();
	            String UnitSealNbr4 = Unit.getUnitSealNbr4();
								
	            Unit.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.unitCustomDFFSealMFestNbr1"), UnitSealNbr1);
	            Unit.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.unitCustomDFFSealMFestNbr2"), UnitSealNbr2);
	            Unit.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.unitCustomDFFSealMFestNbr3"), UnitSealNbr3);
	            Unit.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.unitCustomDFFSealMFestNbr4"), UnitSealNbr4);
	            --*/

	        }           
	      }
	    return Unidades.substring(0, Unidades.length() - 1);
	}

	//CLEAR SELLOS
	private void clearUnitsSeals(BillOfLading BillOfLadingNbr)
	{
		Unit Unit;
		GoodsBl goodsBl;
			
		Set<BlGoodsBl> blGoodsBlSet = BillOfLadingNbr.getBlBlGoodsBls();

		if (blGoodsBlSet != null) {
			
			for (BlGoodsBl blGoodsBl : blGoodsBlSet)
			{
				goodsBl = blGoodsBl.getBlgdsblGoodsBl();
				Unit = goodsBl.getGdsUnit();
			
				Unit.setFieldValue(MetafieldIdFactory.valueOf("unitSealNbr1"), "");
				Unit.setFieldValue(MetafieldIdFactory.valueOf("unitSealNbr2"), "");
				Unit.setFieldValue(MetafieldIdFactory.valueOf("unitSealNbr3"), "");
				Unit.setFieldValue(MetafieldIdFactory.valueOf("unitSealNbr4"), "");
								
			}
				  
		}
		
	}





}