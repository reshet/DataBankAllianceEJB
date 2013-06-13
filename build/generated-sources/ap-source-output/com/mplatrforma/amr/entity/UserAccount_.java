package com.mplatrforma.amr.entity;

import com.mplatrforma.amr.entity.RegisteredUserAnalysisRoot;
import com.mplatrforma.amr.entity.RegisteredUserHistory;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-06-13T23:12:50")
@StaticMetamodel(UserAccount.class)
public class UserAccount_ { 

    public static volatile SingularAttribute<UserAccount, Long> id;
    public static volatile SingularAttribute<UserAccount, RegisteredUserHistory> history;
    public static volatile SingularAttribute<UserAccount, String> name;
    public static volatile SingularAttribute<UserAccount, String> accountType;
    public static volatile SingularAttribute<UserAccount, String> emailAddress;
    public static volatile SingularAttribute<UserAccount, RegisteredUserAnalysisRoot> analysis;
    public static volatile SingularAttribute<UserAccount, String> password;

}