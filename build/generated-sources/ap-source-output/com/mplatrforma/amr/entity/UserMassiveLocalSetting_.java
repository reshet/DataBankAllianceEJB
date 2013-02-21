package com.mplatrforma.amr.entity;

import java.util.ArrayList;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-02-20T16:03:51")
@StaticMetamodel(UserMassiveLocalSetting.class)
public class UserMassiveLocalSetting_ { 

    public static volatile SingularAttribute<UserMassiveLocalSetting, Integer> id;
    public static volatile SingularAttribute<UserMassiveLocalSetting, Integer> weights_use;
    public static volatile SingularAttribute<UserMassiveLocalSetting, Integer> filters_use;
    public static volatile SingularAttribute<UserMassiveLocalSetting, ArrayList> weights_usage;
    public static volatile SingularAttribute<UserMassiveLocalSetting, ArrayList> filters_usage;
    public static volatile SingularAttribute<UserMassiveLocalSetting, Long> research_id;
    public static volatile SingularAttribute<UserMassiveLocalSetting, ArrayList> filters;
    public static volatile SingularAttribute<UserMassiveLocalSetting, Integer> weights_var_id;

}