package com.hit.groovy

import org.apache.log4j.Logger;
import com.navis.inventory.business.api.UnitFinder
import com.navis.framework.business.Roastery
import com.navis.argo.ContextHelper
import com.navis.argo.business.reference.Equipment
import com.navis.services.business.rules.EventType
import com.navis.framework.portal.query.DomainQuery
import com.navis.framework.portal.QueryUtils
import com.navis.services.business.event.Event
import com.navis.services.ServicesEntity
import com.navis.services.ServicesField
import com.navis.framework.persistence.HibernateApi
import com.navis.inventory.business.units.Unit
import com.navis.framework.portal.query.PredicateFactory

/**
 * Created by IntelliJ IDEA.
 * User: gbabu
 * Date: Jul 6, 2011
 * Time: 4:39:24 AM
 * To change this template use File | Settings | File Templates.
 */
class HITGvyDeleteServiceEvent {
  public void execute(Unit inUnit, String inEventId) {

    UnitFinder unitFinder = (UnitFinder) Roastery.getBean(UnitFinder.BEAN_ID);
    EventType inEventType = EventType.findEventType(inEventId);

    if (inUnit == null) {
      LOGGER.warn("Cannot find the unit")
      return;
    }
   
    if (inEventType == null) {
      LOGGER.warn("Event type is not valid")
      return;
    }
    //Find the event
    DomainQuery dq = QueryUtils.createDomainQuery(ServicesEntity.EVENT);
    dq.addDqPredicate(PredicateFactory.eq(ServicesField.EVNT_EVENT_TYPE, inEventType.getPrimaryKey()))
            .addDqPredicate(PredicateFactory.eq(ServicesField.EVNT_APPLIED_TO_PRIMARY_KEY, inUnit.getPrimaryKey()));
    List<Event> eventList = HibernateApi.getInstance().findEntitiesByDomainQuery(dq);
    // Delete the event
    if (eventList != null && !eventList.isEmpty()) {
      for (Object eventObj: eventList) {
        Event event = (Event) eventObj;
        LOGGER.warn("Purging event"+ event)
        HibernateApi.getInstance().delete(event);
      }
    }
  }
  private static final Logger LOGGER = Logger.getLogger(HITGvyDeleteServiceEvent.class);
}
