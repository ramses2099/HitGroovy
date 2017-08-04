package com.hit.groovy;

import com.navis.argo.business.reference.LineOperator;

public class HITGvyGetIdLineOperator {

	 public String execute() {
	     String lineOp = LineOperator.findLineOperatorById('APL');
	     return lineOp;
	}
	
}
