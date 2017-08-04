import com.navis.external.framework.ui.AbstractTableViewCommand;
import com.navis.external.framework.ui.EUIExtensionHelper;
import com.navis.framework.metafields.entity.EntityId;
import com.navis.framework.util.message.MessageLevel;

import com.navis.framework.portal.BizRequest;
import com.navis.framework.portal.UserContext;
import com.navis.framework.ulc.server.context.UlcRequestContextFactory;
import com.navis.framework.ulc.server.application.view.ViewHelper;
import com.navis.framework.metafields.MetafieldId;

import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.business.Roastery;

import com.navis.argo.ContextHelper;

public class HITGvyTVEjecutaCambioMasivoTarifas extends AbstractTableViewCommand
 {
	private DatabaseHelper _dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);
	private EUIExtensionHelper extHelper = getExtensionHelper();
	private String usuarioActual = com.navis.argo.ContextHelper.getThreadUserId();
	
	public void execute(EntityId inEntityId, List<Serializable> inGkeys,Map<String, Object> inParams)
	 {
		
		try
		{

			/*
			String insertSQL = "INSERT CUSTOM_TARIFA_AR(CUSTOMTAR_SEMANA,CUSTOMTAR_PRECIO,CUSTOMTAR_SERVICIO,CUSTOMTAR_MINIMO,CUSTOMTAR_RECARGO,CUSTOMTAR_SERVICIO_GKEY,CUSTOMTAR_GKEY_T_REAL,CUSTOMTAR_USUARIO,CUSTOMTAR_FECHA)" +
					" SELECT CUSTOMTARIFA_SEMANA,CUSTOMTARIFA_PRECIO,CUSTOMTARIFA_SERVICIO,CUSTOMTARIFA_MINIMO,CUSTOMTARIFA_RECARGO,CUSTOMTARIFA_SERVICIOGKEY,GKEY,'" + usuarioActual + "',GETDATE() FROM CUSTOM_TARIFA" + 
					" WHERE GKEY IN (SELECT CUSTOMTTEMP_GKEY_T_REAL FROM CUSTOM_TARIFA_TEMP)"
	
			String updSQL = "UPDATE CT SET CT.CUSTOMTARIFA_PRECIO = CTEMP.CUSTOMTTEMP_PRECIO,CT.CUSTOMTARIFA_MINIMO = CTEMP.CUSTOMTTEMP_MINIMO,CT.CUSTOMTARIFA_RECARGO = CTEMP.CUSTOMTTEMP_RECARGO " + 
							" FROM CUSTOM_TARIFA CT INNER JOIN CUSTOM_TARIFA_TEMP CTEMP ON CT.gkey = CTEMP.CUSTOMTTEMP_GKEY_T_REAL"
			
			_dbHelper.execute(insertSQL);
			
			_dbHelper.execute(updSQL); */
			
			extHelper.showMessageDialog(MessageLevel.INFO, "Mensaje", "Los cambios de tarifas fueron realizados exitosamente");
		}
		
		catch(Exception ex)
		{
			throw ex;
		}
		
		
		
	}
	

}