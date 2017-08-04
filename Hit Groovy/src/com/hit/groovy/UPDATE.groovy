import com.navis.framework.metafields.MetafieldIdFactory
import com.navis.billing.business.model.Customer;


public class ActualizaClienteProgramaticamente
{
	public String execute()
	{
		Customer cliente = Customer.hydrate(49176L);

		//cliente.setFieldValue(MetafieldIdFactory.valueOf("customFlexFields.custCustomDFFEJEMPLO"), "Valor Nuevo");

		String valor  = cliente.getField(MetafieldIdFactory.valueOf("customFlexFields.custCustomDFFEJEMPLO")).toString();

		return valor
		

	}
}