package com.hit.groovy

import com.navis.framework.presentation.lovs.Lov
import com.navis.external.framework.ui.lov.AbstractExtensionLovFactory
import com.navis.framework.presentation.lovs.list.DomainQueryLov
import com.navis.framework.portal.query.DomainQuery
import com.navis.framework.presentation.lovs.Style
import com.navis.framework.portal.query.PredicateFactory
import com.navis.security.SecurityEntity
import com.navis.security.SecurityField
import com.navis.framework.portal.query.QueryFactory
import com.navis.reference.ReferenceEntity
import com.navis.reference.ReferenceField
import com.navis.framework.portal.query.OuterQueryMetafieldId
import com.navis.external.framework.ui.lov.ELovKey
import com.navis.framework.portal.QueryUtils

public class HITGvyExtensionLOVClieneteRealcion extends AbstractExtensionLovFactory
{

	public Lov getLov(ELovKey inKey)
	 {
			   
		DomainQuery dq = QueryUtils.createDomainQuery("Customer")
		dq.addDqField("custId");
		dq.addDqField("custName");

		//final DomainQuery dq =QueryFactory.createDomainQuery(DynamicHibernatingEntity.customEntityFields.CustomServicio);
		//dq.addDqField(customEntityFields.customservicCodigo);

		DomainQueryLov dqLov = new DomainQueryLov(dq, Style.LABEL_ONLY);

		return dqLov;
	}
}