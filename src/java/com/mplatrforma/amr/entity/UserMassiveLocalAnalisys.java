/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package com.mplatrforma.amr.entity;


import java.util.ArrayList;

import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.*;


/**
 * The UserAccount persistence-capable class holds information about a
 * given user of Connectr.  A bidirectional JDO owned relationship is used to
 * manage its child Friend data objects.
 */
@Entity
@Table(name = "USERMASSIVEANALISYS")
//@NamedQueries({
//    @NamedQuery(name = "UserAccount.getDefUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.accountType = :type AND x.password = :pswd"),
//    @NamedQuery(name = "UserAccount.getAccount", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.password = :pswd")
//
//})
public class UserMassiveLocalAnalisys implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
   
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private UserMassiveLocalSetting setting;
    private ArrayList<Double> distribution;
    private String distr_type;
   
    @ManyToOne
    private Var var_involved_first;
    @ManyToOne
    private Var var_involved_second;
    
    
    
    
  public UserMassiveLocalAnalisys() {
	  //accountType = "defaultUser";
  }
    public UserMassiveLocalAnalisys(UserAnalysisSaveDTO dto) {
        setting = new UserMassiveLocalSetting(dto.getSeting());
        distribution = dto.getDistribution();
       // var_involved_first = 
	  //accountType = "defaultUser";
  }
  public UserMassiveLocalAnalisys(EntityManager emm) {
	  //accountType = "defaultUser";
          this.em = emm;
  }
  
  public UserAnalysisSaveDTO toDTO()
  {
      UserAnalysisSaveDTO dto = new UserAnalysisSaveDTO();
      dto.setId(id);
      dto.setDistribution(distribution);
      dto.setVar_1(var_involved_first.toDTO_DetailedNoCalc(em));
      dto.setVar_2(var_involved_second.toDTO_DetailedNoCalc(em));
      
      return dto;
  }
  public Integer getId() {
    return id;
  }

    /**
     * @return the setting
     */
    public UserMassiveLocalSetting getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(UserMassiveLocalSetting setting) {
        this.setting = setting;
    }

    /**
     * @return the distribution
     */
    public ArrayList<Double> getDistribution() {
        return distribution;
    }

    /**
     * @param distribution the distribution to set
     */
    public void setDistribution(ArrayList<Double> distribution) {
        this.distribution = distribution;
    }

    /**
     * @return the distr_type
     */
    public String getDistr_type() {
        return distr_type;
    }

    /**
     * @param distr_type the distr_type to set
     */
    public void setDistr_type(String distr_type) {
        this.distr_type = distr_type;
    }

 
 
}
