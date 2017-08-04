
import com.navis.external.framework.entity.AbstractEntityLifecycleInterceptor;

import com.navis.external.framework.entity.EEntityView;
import com.navis.external.framework.util.EFieldChanges;
import com.navis.external.framework.util.EFieldChangesView;
import com.navis.external.framework.ECallingContext;
import com.navis.orders.business.eqorders.Booking;
import com.navis.orders.OrdersField;
 
public class HITGvyActualizaAgentBooking extends AbstractEntityLifecycleInterceptor 
{
	
		@Override
		public void onUpdate(EEntityView inEntity, EFieldChangesView inOriginalFieldChanges, EFieldChanges inMoreFieldChanges) 
		{
			inMoreFieldChanges.setFieldChange(OrdersField.EQO_NOTES, 'Booking cambiado por CE: HITGvyActualizaAgentBooking01');
			//cambio aqui test
			return;
			
		}
}

