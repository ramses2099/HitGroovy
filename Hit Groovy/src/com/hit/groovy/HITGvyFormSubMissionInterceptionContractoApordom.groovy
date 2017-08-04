package com.hit.groovy

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.navis.external.framework.ui.AbstractFormSubmissionCommand
import com.navis.external.framework.ui.EUIExtensionHelper
import com.navis.external.framework.util.EFieldChanges;
import com.navis.framework.metafields.entity.EntityId;
import com.navis.framework.util.BizViolation
import com.navis.framework.util.message.MessageCollector;
import com.navis.framework.util.message.MessageLevel;
import com.navis.framework.util.internationalization.PropertyKey;
import com.navis.framework.util.internationalization.PropertyKeyFactory;
import com.navis.framework.metafields.MetafieldIdFactory;
import com.navis.argo.ContextHelper;


public class HITGvyFormSubMissionInterceptionContractoApordom extends AbstractFormSubmissionCommand{

	@Override
    public void submit(String inVariformId, EntityId inEntityId, List<Serializable> inGkeys, EFieldChanges inOutFieldChanges,
                       EFieldChanges inNonDbFieldChanges, Map<String,Object> inParams) {
				
			try {

				if(inVariformId == "CUSTOM_CONTRATO_APORDOM_ADD"){
					
					EUIExtensionHelper extHelper = getExtensionHelper();
					
					String NoContrato  = inOutFieldChanges.findFieldChange(MetafieldIdFactory.valueOf("customEntityFields.customapordoNoContrato"));
					
					if(NoContrato == ""){
						extHelper.showMessageDialog(MessageLevel.SEVERE, "Error", "Debe especificar un valor para el campo No Contracto");
						return;
					}
				}
				
				
			} catch (BizViolation inBizViolation) {
				
				MessageCollector messageCollector = ContextHelper.getThreadMessageCollector();
				if (messageCollector != null) {
				   messageCollector.appendMessage(MessageLevel.SEVERE, PropertyKeyFactory.valueOf(inBizViolation.getMessage()), null, inBizViolation.getParms());
				 }
		   
			
			}
			
	
	}

	
}
