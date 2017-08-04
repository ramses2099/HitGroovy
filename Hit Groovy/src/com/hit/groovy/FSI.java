import com.navis.external.framework.ui.AbstractFormSubmissionCommand;
import com.navis.external.framework.ui.EUIExtensionHelper;
import com.navis.framework.util.ValueObject;
import com.navis.framework.util.message.MessageCollector;
import com.navis.framework.util.message.MessageLevel;
import com.navis.framework.metafields.entity.EntityId;
import com.navis.external.framework.util.EFieldChange;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.framework.metafields.MetafieldIdFactory
import com.navis.framework.metafields.MetafieldId;
import com.navis.framework.ulc.server.application.view.ViewHelper;
import com.navis.framework.metafields.entity.MetafieldEntity;
import com.navis.framework.portal.BizResponse;
import com.navis.framework.portal.CrudBizDelegate;
import com.navis.framework.portal.FieldChange;
import com.navis.framework.portal.FieldChanges;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.metafields.entity.EntityIdFactory;
import com.navis.argo.ContextHelper;
import com.navis.framework.presentation.context.PresentationContextUtils;
import com.navis.framework.presentation.context.RequestContext;
import com.navis.framework.presentation.util.FrameworkUserActions;

import java.io.Serializable;
import java.util.*;

import com.navis.argo.business.api.GroovyApi;


import com.navis.framework.persistence.DatabaseHelper;
import com.navis.framework.business.Roastery;

import com.navis.external.framework.ui.AbstractTableViewCommand;
import com.navis.framework.FrameworkPropertyKeys;
import com.navis.framework.metafields.entity.EntityId;
import com.navis.framework.presentation.internationalization.HardCodedResourceKey;
import com.navis.framework.ulc.server.application.controller.ControllerHelper;
import com.navis.framework.ulc.server.application.controller.table.DefaultVariformTableViewController;
import com.navis.framework.ulc.server.application.view.ViewHelper;
import com.navis.framework.ulc.server.extension.ULCCarinaDialog;

import com.navis.framework.util.BizViolation;
import com.navis.framework.util.internationalization.PropertyKey;
import com.navis.framework.util.internationalization.PropertyKeyFactory;
import com.navis.argo.ContextHelper;

import java.util.Map;

public class HITGvyMandarForm extends AbstractFormSubmissionCommand {

	private DatabaseHelper _dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);

	
    public void submit(String inVariformId, EntityId inEntityId, List<Serializable> inGkeys, EFieldChanges inOutFieldChanges,
                       EFieldChanges inNonDbFieldChanges, Map<String,Object> inParams) 
    {

    	EUIExtensionHelper extHelper = getExtensionHelper();
		String dialogTitle = "Este es un ejemplo de ejecucion de Form Submission Interception";
		extHelper.showMessageDialog(MessageLevel.SEVERE, dialogTitle, "Este es un ejemplo de ejecucion de Form Submission Interception");
		
    	
		/*
    		try
    		{
    			
    			if (inVariformId == "CUSTOM_BIL086-4")
    			{
                	EUIExtensionHelper extHelper = getExtensionHelper();
            		String dialogTitle = "Error",actualizaPrecio,actualizaMinimo,actualizaRecargo,insertaSQL,claves = inGkeys.toString().replace("[","(").replace("]", ")");
            		
            		PropertyKey CUSTOM_ERROR_CAMPO_BLANCO;
            		
            		actualizaPrecio = retornaCampoFormateado(1,inOutFieldChanges.findFieldChange(MetafieldIdFactory.valueOf("customEntityFields.customPorcentajeAumentoPrecio")));
            		actualizaMinimo = retornaCampoFormateado(2,inOutFieldChanges.findFieldChange(MetafieldIdFactory.valueOf("customEntityFields.customPorcentajeAumentoMinimo")));
            		actualizaRecargo = retornaCampoFormateado(3, inOutFieldChanges.findFieldChange(MetafieldIdFactory.valueOf("customEntityFields.customPorcentajeAumentoRecargo")));
            		
            		if (actualizaPrecio == "(ERROR)" || actualizaMinimo == "(ERROR)" || actualizaRecargo == "(ERROR)")
            		{
            			extHelper.showMessageDialog(MessageLevel.SEVERE, dialogTitle, "Debe especificar un valor para los campos seleccionados");
            			return;
            		}
            		
            		//Limpia y llena la tabla temporal de cambio de tarifa
            		_dbHelper.execute("DELETE CUSTOM_TARIFA_TEMP");
        			_dbHelper.execute(construyeSentenciaSQL(actualizaPrecio,actualizaMinimo,actualizaRecargo,claves));

            		
        			// Abrea la vista preliminar con los resultados del cambio antes de su aplicacion
        			openView("CUSTOM_TARIFA_TEMP", "Preliminar cambio masivo de tarifa");
    				
    			}
        		
    		}
    		
    		catch(BizViolation inBizViolation)
    		{
			 	MessageCollector messageCollector = ContextHelper.getThreadMessageCollector();
				 if (messageCollector != null) {
				        messageCollector.appendMessage(MessageLevel.SEVERE, PropertyKeyFactory.valueOf(inBizViolation.getMessage()), null, inBizViolation.getParms());
				      }	  
    			
    		}
    		*/
    		

    }
    
    private String construyeSentenciaSQL(String actualizaPrecio,String actualizaMinimo,String actualizaRecargo,String claves)
    {
    	
		return "INSERT CUSTOM_TARIFA_TEMP(CUSTOMTTEMP_PRECIO,CUSTOMTTEMP_MINIMO,CUSTOMTTEMP_RECARGO,CUSTOMTTEMP_SEMANA,CUSTOMTTEMP_SERVICIO,CUSTOMTTEMP_SERVICIO_GKEY,CUSTOMTTEMP_GKEY_T_REAL)" + 
				 "SELECT " + actualizaPrecio + "," + actualizaMinimo + "," + actualizaRecargo + ", CUSTOMTARIFA_SEMANA, CUSTOMTARIFA_SERVICIO, CUSTOMTARIFA_SERVICIOGKEY,gkey FROM CUSTOM_TARIFA WHERE gkey in " + claves ;

    }
    
    private String retornaCampoFormateado(int indice,FieldChange cambioCampo)
    {
    		
        	switch(indice)
        	{
        		case 1: 
        			return cambioCampo == null?"CUSTOMTARIFA_PRECIO":cambioCampo.getNewValue() != null?"CUSTOMTARIFA_PRECIO + (CUSTOMTARIFA_PRECIO * " + cambioCampo.getNewValue() / 100 + ")":"(ERROR)";
        			break;

        		case 2: 
        			return cambioCampo == null?"CUSTOMTARIFA_MINIMO":cambioCampo.getNewValue() != null?"CUSTOMTARIFA_MINIMO + (CUSTOMTARIFA_MINIMO * " + cambioCampo.getNewValue() / 100 + ")":"(ERROR)";
        			break;

        		case 3: 
        			return cambioCampo == null?"CUSTOMTARIFA_RECARGO":cambioCampo.getNewValue() != null?"CUSTOMTARIFA_RECARGO + (CUSTOMTARIFA_RECARGO * " + cambioCampo.getNewValue() / 100 + ")":"(ERROR)";
        			break;
   		
        	}
    	
    }
    
    
    private Serializable buscarTarifaPorPK(String inEntityName, Serializable inPrimaryKey) 
    {
    	
  	    MetafieldId field = MetafieldIdFactory.valueOf("customEntityFields.customtarifaPrecio");
        Serializable taPk = (Serializable) ViewHelper.getEntityFieldValue("com.hit.almacenaje.CustomTarifa", inPrimaryKey, field);
        
        
        return taPk;
        
    }

	private void openView(String inVariformId, String inTitle) 
	{
		DefaultVariformTableViewController controller = new DefaultVariformTableViewController(inVariformId);
		ULCCarinaDialog dialog = ViewHelper.createDialog(controller.getPanel().getComponent(),HardCodedResourceKey.valueOf(FrameworkPropertyKeys.LABEL_ACTION, inTitle),false);
		dialog.setVisible(true);
	}
}