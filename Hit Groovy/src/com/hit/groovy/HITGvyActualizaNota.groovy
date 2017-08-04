// Prueba Script Runner
import com.navis.argo.business.api.GroovyApi;
import com.navis.orders.business.eqorders.Booking;
import com.navis.orders.ServiceOrderField;
import com.navis.orders.OrdersField;

public class HITGvyActualizaNota {

	public void execute(){
		  //UPDATE ENTITYS
	      Booking bk  = Booking.hydrate(263980);
		     
		  //bk.setFieldValue(ServiceOrderField.EQO_QUANTITY,3L);
			
		  bk.setFieldValue(ServiceOrderField.EQO_NOTES,"Prueba de Update");
		  	  
		  // Forma 2 - Usando un metodo de la entidad si esta disponible
		  //unit.updateRemarks("Se proceso una foto para evidencia - METODO UpdateRemarks");

	   }

}