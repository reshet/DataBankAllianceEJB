package com.mplatrforma.amr.entity;

import com.mplatrforma.amr.entity.MetaUnitEntityItem;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-05-13T23:11:55")
@StaticMetamodel(MetaUnitMultivaluedEntity.class)
public class MetaUnitMultivaluedEntity_ extends MetaUnitMultivalued_ {

    public static volatile SingularAttribute<MetaUnitMultivaluedEntity, Long> isMultiSelected;
    public static volatile CollectionAttribute<MetaUnitMultivaluedEntity, MetaUnitEntityItem> items;

}