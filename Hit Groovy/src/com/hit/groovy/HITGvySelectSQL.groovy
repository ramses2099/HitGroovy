package com.hit.groovy

/*
 * Sample Groovy Class.  As below, your class must have a no-arg execute() method that returns a String.
 */
 
 import com.navis.framework.portal.query.PredicateFactory
 import com.navis.framework.persistence.DatabaseHelper;
 import com.navis.framework.persistence.HibernatingEntity;
 import com.navis.framework.metafields.MetafieldIdFactory;
 import com.navis.framework.business.Roastery;
 import java.util.regex.Pattern;
 
 class HITGvySelectSQL {
 
 private DatabaseHelper _dbHelper;
 
	 public String execute() {
 
		  _dbHelper = (DatabaseHelper) Roastery.getBean(DatabaseHelper.BEAN_ID);
		   String query = "SELECT TOP 1 id  FROM  dbo.inv_unit where gkey = 47549";
		   List Lista = _dbHelper.queryForList(query);
		   String[] rows = Lista.toString().replace("[", "").replace("]", "").split(Pattern.quote(","));
 
		   return rows[0].split(Pattern.quote(":"))[1];
	 }
 }