package com.mplatrforma.amr.entity;

import com.mplatrforma.amr.entity.UserMassiveLocalSetting;
import java.util.ArrayList;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-06-13T23:12:50")
@StaticMetamodel(RegisteredUserHistory.class)
public class RegisteredUserHistory_ { 

    public static volatile SingularAttribute<RegisteredUserHistory, String> id;
    public static volatile ListAttribute<RegisteredUserHistory, UserMassiveLocalSetting> local_research_settings;
    public static volatile SingularAttribute<RegisteredUserHistory, ArrayList> favourite_massives;

}