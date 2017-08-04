package com.hit.groovy

import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor
import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;
import com.navis.framework.util.BizViolation;

import com.navis.argo.ContextHelper;
import com.navis.framework.email.EmailMessage;
import com.navis.framework.email.EmailManager;
import com.navis.framework.business.Roastery;
import com.navis.inventory.business.units.Unit


public class HITGvyEnviaEmailPredviseUnit extends AbstractEntityLifecycleInterceptor {

	
	@Override
	public void onCreate(EEntityView inEntityView, EFieldChangesView inOriginalFieldChanges, EFieldChanges inMoreFieldChanges) {
		try{
						
			// Procesar
			//String[] copia;
			//copia = ["correo1@hit.com","correo2@hit.com","correo3@hit.com","correo4@hit.com"];
			
			Unit unit = (Unit)inEntityView._entity;
			
			EmailMessage msg = new EmailMessage(ContextHelper.getThreadUserContext());
			msg.setTo("jose.encarnacion@hit.com.do");
			//msg.setCc(copia);
			msg.setFrom("N4-Navis@hit.com");
			
			msg.setSubject("Alerta: Pre-Advise envio de correo");
			msg.setText("Unit Pre-Advised : " + unit.getUnitId());
			
			EmailManager manager = (EmailManager) Roastery.getBean("emailManager");
			manager.sendEmail(msg);
		  
						
		}catch(BizViolation ex){
	
			log("ERROR HITGvyEnviaEmailPredviseUnit : " + ex.getMessage());
		}
		
		
	}

	@Override
	public void log(String arg0) {
		// TODO Auto-generated method stub
		super.log(arg0);
	}

	
	
	
	
}
