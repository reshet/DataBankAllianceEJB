package com.mplatrforma.amr.entity;

import com.mplatrforma.amr.entity.UserMassiveLocalSetting;
import com.mplatrforma.amr.entity.Var;
import java.util.ArrayList;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2012-11-19T13:16:13")
@StaticMetamodel(UserMassiveLocalAnalisys.class)
public class UserMassiveLocalAnalisys_ { 

    public static volatile SingularAttribute<UserMassiveLocalAnalisys, Integer> id;
    public static volatile SingularAttribute<UserMassiveLocalAnalisys, UserMassiveLocalSetting> setting;
    public static volatile SingularAttribute<UserMassiveLocalAnalisys, String> distr_type;
    public static volatile SingularAttribute<UserMassiveLocalAnalisys, Var> var_involved_second;
    public static volatile SingularAttribute<UserMassiveLocalAnalisys, ArrayList> distribution;
    public static volatile SingularAttribute<UserMassiveLocalAnalisys, Var> var_involved_first;

}