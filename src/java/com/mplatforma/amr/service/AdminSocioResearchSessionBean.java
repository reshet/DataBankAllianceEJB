/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.*;
import com.mplatforma.amr.service.remote.AdminSocioResearchBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;

import com.mplatrforma.amr.entity.*;
import com.mresearch.databank.jobs.*;
import com.mresearch.databank.shared.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.elasticsearch.common.mvel2.optimizers.impl.refl.nodes.ArrayLength;
import org.opendatafoundation.data.FileFormatInfo;
import org.opendatafoundation.data.FileFormatInfo.Format;
import org.opendatafoundation.data.mod.SPSSFile;
import org.opendatafoundation.data.mod.SPSSFileException;
import org.opendatafoundation.data.mod.SPSSNumericVariable;
import org.opendatafoundation.data.mod.SPSSStringVariable;
import org.opendatafoundation.data.mod.SPSSVariable;
import org.w3c.dom.Document;

import static argo.jdom.JsonNodeBuilders.*;
import argo.saj.InvalidSyntaxException;
import static argo.format.JsonNumberUtils.asBigDecimal;
import java.math.BigDecimal;

/**
 *
 * @author reshet
 */
@WebService
@Stateless(mappedName="AdminSocioResearchRemoteBean",name="AdminSocioResearchRemoteBean")
public class AdminSocioResearchSessionBean implements AdminSocioResearchBeanRemote{

    static
    {
         Locale locale = Locale.getDefault();
           System.out.println("Before setting, Locale is = " + locale);
         locale = new Locale("ru","RU");
        //  // Setting default locale  
        // // locale = Locale.ITALY;
         Locale.setDefault(locale);
          System.out.println("After setting, Locale is = " + locale);
    }
    
    private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();
    private static final JdomParser JDOM_PARSER = new JdomParser();
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private RxStorageBeanRemote store;
    
    @EJB
    private UserSocioResearchBeanRemote user_bean;
   
    public UserAccountDTO updateAccountResearchState(UserAccountDTO dto) {
        UserAccount account;
        UserAccountDTO returnBack = dto;
        account = em.find(UserAccount.class,dto.getId());
        if (account != null)
        {
                account.updateAccountResearchState(dto);
                returnBack = UserAccount.toDTO(account);
        }
        return returnBack;
    }

     

    @Override
    public Boolean deleteResearch(long id) {
        try
        {
            SocioResearch r = em.find(SocioResearch.class, id);
            ArrayList<Long> r_id = new ArrayList<Long>();
            r_id.add(r.getID());
            launchDeleteIndexing(r_id, "research");
            launchDeleteIndexing(Var.getResearchVarsIDs(em, id), "sociovar");
            Var.deleteResearchVars(em,id);
            DatabankStartPage sp = DatabankStartPage.getStartPageSingleton(em);
            if(sp.getRes().contains(r))sp.getRes().remove(r);
            em.persist(sp);
            em.remove(r);
            return true;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
       // throw new UnsupportedOperationException("Not supported yet.");
    }

     private SocioResearch addResearch(SocioResearchDTO researchDTO) {
        SocioResearch research = null;
        try {
          research = new SocioResearch(researchDTO,em);
          em.persist(research);
          research.updateEntityID(research.getID(),research.getId_search_repres(), em);
         // currentUser.getFriends().add(friend);
        } finally {
        }
        return research;
      }
    @Override
    public SocioResearchDTO updateResearch(SocioResearchDTO rDTO) {
       // throw new UnsupportedOperationException("Not supported yet.");
            if (rDTO.getId() == 0){ // create new
              SocioResearch newResearch = addResearch(rDTO);
              return newResearch.toDTO();
            }

            SocioResearch research = null;
            try {
             
                
              research = em.find(SocioResearch.class, rDTO.getId());
              research.updateFromDTO(rDTO,em);
//              addSSE("SocioResearch","gengeath", rDTO.getGen_geathering());
//              addSSE("SocioResearch","method", rDTO.getMethod());
//              ArrayList<String> concepts = rDTO.getConcepts();
//              if(concepts!=null && concepts.size()>0)
//              {
//                  for(String concept:concepts)
//                  {
//                          addSSE("SocioResearch","concept", concept);
//                  }  
//              }
             // ArrayList<String> researchers = rDTO.getResearchers();
//              if(researchers!=null && researchers.size()>0)
//              {
//                  for(String researcher:researchers)
//                  {
//                          addSSE("SocioResearch","researcher", researcher);
//                  }
//              }
             // addSSE("SocioResearch","org_impl", rDTO.getOrg_impl_name());
              //addSSE("SocioResearch","org_order", rDTO.getOrg_order_name());
             // ArrayList<String> pubs = rDTO.getPublications();
//              if(pubs!=null && pubs.size()>0)
//              {
//                  for(String pub:pubs)
//                  {
//                          addSSE("SocioResearch","publication", pub);
//                  }
//              }
             // addSSE("SocioResearch","selection_complexity", rDTO.getSel_complexity());
             // addSSE("SocioResearch","selection_randomity", rDTO.getSel_randomity());
              
              launchIndexing(rDTO);
              
              int bb = 2;
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            }
            
            return rDTO;

    }

    @Override
    public void updateVar(VarDTO_Detailed rDTO) {
       // throw new UnsupportedOperationException("Not supported yet.");
            

            Var var = null;
            try {
             
                
              var = em.find(Var.class, rDTO.getId());
              var.updateFromDTO(rDTO,em);
             
              em.persist(var);
              launchIndexingVar(rDTO);
              
              int bb = 2;
              
              //findVarsLikeThis(rDTO.getId(), new ComparativeSearchParamsDTO());
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            }
    }
    
    @Resource(mappedName="jms/myQCF")
    private  QueueConnectionFactory connectionFactory;

    @Resource(mappedName="jms/spss_parse")
    private  Queue queue;
    
//    @Resource(mappedName="jms/ES_index")
//    private  Queue index_queue;
    
     private QueueConnection connection;
    private QueueSession session;
    QueueSender q_sender;
    @PostConstruct
    private void init()
    {
     try {
             connection = connectionFactory.createQueueConnection();
             session = connection.createQueueSession(false, 0);
             q_sender = session.createSender(queue);

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    @PreDestroy
    private void release()
    {
       try {
            q_sender.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
     private void launchIndexingVar(VarDTO_Detailed dto)
    {
         try {
            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index SocioResearch var");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexVarJobFast job = new IndexVarJobFast(dto);
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
     
   
    
    private void launchIndexing(SocioResearchDTO dto)
    {
         try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index SocioResearch");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexResearchJob job = new IndexResearchJob(dto.getId());
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
//            q_sender.close();
//            connection.close();
            //response.sendRedirect("ListNews");

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    
      private void launchDeleteIndexing(ArrayList<Long> ids,String type)
         {
         try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to delete index "+type+" "+ids.size()+" elements");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            
            DeleteIndexiesJob job = new DeleteIndexiesJob(ids, type);
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
//            q_sender.close();
//            connection.close();
            //response.sendRedirect("ListNews");

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public long parseSPSS(long blobkey, long length) {
        
        try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to parse SPSS file");
            // here we create NewsEntity, that will be sent in JMS message
            ParseSpssJob job = new ParseSpssJob(blobkey, length);
          
            message.setObject(job);   
          //  message.setJMSDestination(queue);
            q_sender.send(message);
//            q_sender.close();
//            connection.close();
//            //response.sendRedirect("ListNews");

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
            
	return 0;
    }

   
    @Override
    public SocioResearchDTO updateResearchGrouped(SocioResearchDTO rDTO) {
        if (rDTO.getId() == 0){ // create new
            return rDTO;
        }

        SocioResearch research = null;
        try {
          research = em.find(SocioResearch.class, rDTO.getId());
          research.updateFromDTOGrouped(rDTO,em);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            
        }
        return rDTO;
    }

    @Override
    public VarDTO_Detailed generalizeVar(long var_id, ArrayList<Long> gen_var_ids,UserAccountDTO user) {
            VarDTO_Detailed detailed = null;
	    Var dsVar, detached;

	    try {
	      dsVar = em.find(Var.class, var_id);
	      dsVar.setGeneralized_var_ids(gen_var_ids);
	     // UserAccountDTO watching_user = (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	      detailed = dsVar.toDTO_Detailed(user,em);
	    } finally {
	    }
	    
	return detailed;
    }

    @Override
    public long addOrgImpl(OrgDTO dto) {
          Organization org = null;
	  long org_id = 0;
	    try {
	      org = new Organization(dto);
	      em.persist(org);
	      org_id = org.getId();
	    } finally {
	    }
	 return org_id;
    }

    @Override
    public Boolean addFileToAccessor(long id_research, long id_file, String desc, String category) {
	    SocioResearch dsResearch, detached;
            try {
	      dsResearch = em.find(SocioResearch.class, id_research);
	      ResearchFilesDTO dto = dsResearch.toFilesDTO(em);
	      dto.addFile(category, desc, id_file);
	      dsResearch.updateFileAccessor(em,dto);
	    } finally {
	    }
	 return true;
    }

    @Override
    public Boolean deleteFileFromAccessor(long id_research, long id_file) {
	    SocioResearch dsResearch, detached;
	    try {
	      dsResearch = em.find(SocioResearch.class, id_research);
	      ResearchFilesDTO dto = dsResearch.toFilesDTO(em);
	      dto.deleteFile(id_file);
	      dsResearch.updateFileAccessor(em,dto);
	      return store.deleteFile(id_file);
	    } finally {
                return false;
	    }
    }

    @Override
    public Boolean addSSE(String clas, String kind, String value) {
       SingleStringEntity entity = null;
        try {
          // for this version of the app, just get hardwired 'default' user
          //UserAccount currentUser = UserAccount.getDefaultUser(); // detached object
          //currentUser = pm.makePersistent(currentUser); // attach
            if(value!=null && !value.equals(""))
            {
                List<SingleStringEntity> res = SingleStringEntity.getMatchingFull(em, clas, kind, value);
                if(res.isEmpty())
                {
                   entity = new SingleStringEntity();
                   entity.setClas(clas);
                   entity.setKind(kind);
                   entity.setContents(value);
                   em.persist(entity);
                }
            }
            return true;
        } finally {
            return false;
        }
    }

    @Override
    public Boolean updateFileAccessor(long research_id, ResearchFilesDTO dto) {
        SocioResearch research = null;
        ResearchFilesAccessor accessor;
        try {
          research = em.find(SocioResearch.class, research_id);
          accessor = em.find(ResearchFilesAccessor.class,research.getFile_accessor_id());
          accessor.updateFromDTO(dto);
          return true;
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
            return false;
        }
    }

    
    
    private MetaUnitMultivalued updateMultivaluedUnitFromDTO(MetaUnitDTO dto)
    {
        MetaUnitMultivalued struct = null;
        if(dto instanceof MetaUnitMultivaluedStructureDTO)
        {
            struct = em.find(MetaUnitMultivaluedStructure.class, dto.getId());
            if(struct == null)
            {
                struct = new MetaUnitMultivaluedStructure();
                em.persist(struct);
            }
        }
        if(dto instanceof MetaUnitMultivaluedEntityDTO)
        {
            struct = em.find(MetaUnitMultivaluedEntity.class, dto.getId());
             if(struct == null)
            {
                struct = new MetaUnitMultivaluedEntity();
                ((MetaUnitMultivaluedEntity)struct).setIsMultiSelected(((MetaUnitMultivaluedEntityDTO)dto).isIsMultiselected()?1:0);
                em.persist(struct);
            }
        }

            MetaUnitMultivaluedDTO m_dto = (MetaUnitMultivaluedDTO)dto;
            
            struct.setIsCatalogizable(m_dto.isIsCatalogizable()?1:0);
            struct.setIsSplittingEnabled(m_dto.isIsSplittingEnabled()?1:0);
            struct.setTagged_entities(m_dto.getTagged_entities());
            struct.setUnique_name(dto.getUnique_name());
            struct.setDescription(dto.getDesc());
            int index = 0;
            if(m_dto.getSub_meta_units()!=null)
            for(int j = 0;j< m_dto.getSub_meta_units().size();j++)
            {
                MetaUnitDTO dt = m_dto.getSub_meta_units().get(j);
                if(struct.sub_meta_units_contains(dt.getId()))
                {
                    MetaUnit m_unit = struct.sub_meta_units_get(dt.getId());
                    int index_old = struct.sub_meta_units_get_order(dt.getId());
                    if(index != index_old)
                    {
                        ArrayList<MetaUnit> u = new ArrayList<MetaUnit>();
                        for(MetaUnit un:struct.getSub_meta_units())
                        {
                            u.add(un);
                        }
                        MetaUnit m_unit_sw = u.get(index);
                        u.set(index, m_unit);
                        u.set(index_old, m_unit_sw);
                        struct.setSub_meta_units(u);
                    }
                    
                    m_unit.updateFromDTO(dt);
                    if(m_unit instanceof MetaUnitMultivalued)
                    {
                        updateMultivaluedUnitFromDTO(dt);
                    }
       
                }else
                {
                    MetaUnit new_unit = null;
                    
                    if(dt instanceof MetaUnitDateDTO){new_unit = new MetaUnitDate();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitDoubleDTO){new_unit = new MetaUnitDouble();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitIntegerDTO){new_unit = new MetaUnitInteger();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitStringDTO){new_unit = new MetaUnitString();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitFileDTO){new_unit = new MetaUnitFile();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitMultivaluedStructureDTO)
                        {new_unit = new MetaUnitMultivaluedStructure();new_unit.updateFromDTO(dt);updateMultivaluedUnitFromDTO(dt);}
                    if(dt instanceof MetaUnitMultivaluedEntityDTO)
                        {new_unit = new MetaUnitMultivaluedEntity();new_unit.updateFromDTO(dt);updateMultivaluedUnitFromDTO(dt);}
                    
                    struct.getSub_meta_units().add(new_unit);
                }
                index++;
            }
            em.merge(struct);
            return struct;
           // MetaUnitMultivaluedStructure str = em.find(MetaUnitMultivaluedStructure.class, dto.getId());
    }
    
    @Override
    public Boolean addMetaUnit(long parent_id, MetaUnitDTO dto) {
        MetaUnitMultivalued unit;
        MetaUnit new_unit = null;
        if(dto instanceof MetaUnitDateDTO){new_unit = new MetaUnitDate();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitDoubleDTO){new_unit = new MetaUnitDouble();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitIntegerDTO){new_unit = new MetaUnitInteger();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitStringDTO){new_unit = new MetaUnitString();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitFileDTO){new_unit = new MetaUnitFile();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitMultivaluedStructureDTO){new_unit= updateMultivaluedUnitFromDTO(dto);}
        if(dto instanceof MetaUnitMultivaluedEntityDTO){new_unit= updateMultivaluedUnitFromDTO(dto);}
        
        try
        {
            unit = em.find(MetaUnitMultivalued.class, parent_id);    
            unit.getSub_meta_units().add(new_unit);
            em.persist(unit);
            return true;
        }
        finally
        {
        }
    }



    @Override
    public void updateMetaUnitStructure(MetaUnitDTO dto) {
        MetaUnit unit;
        try
        {
            unit = em.find(MetaUnit.class, dto.getId());    
            unit.updateFromDTO(dto);
            if(unit instanceof MetaUnitMultivalued) updateMultivaluedUnitFromDTO(dto);
            em.persist(unit);
            //return true;
        }
        finally
        {
        }
    }

    @Override
    public void addEntityItem(Long entity_id, String value, HashMap<String, String> filling) {
        MetaUnitMultivaluedEntity entity = em.find(MetaUnitMultivaluedEntity.class, entity_id);
        MetaUnitEntityItem item = new MetaUnitEntityItem(value);
        item.setMapped_values(filling);
        entity.getItems().add(item);
        em.persist(entity);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteMetaUnit(Long id, Long unit_parent_id) {
        MetaUnit unit;
        MetaUnitMultivalued punit;
        try
        {
            unit = em.find(MetaUnit.class, id);    
            punit = em.find(MetaUnitMultivalued.class, unit_parent_id);
            punit.getSub_meta_units().remove(unit);
            em.persist(punit);
            em.remove(unit);
        }
        finally
        {
        }
    }

    @Override
    public void editEntityItem(Long id, String value, HashMap<String, String> filling) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id);
        item.setValue(value);
        item.setMapped_values(filling);
    }

    
    @Override
    public void deleteEntityItem(Long id, Long entity_id) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id);
        MetaUnitMultivaluedEntity ent = em.find(MetaUnitMultivaluedEntity.class, entity_id);
        ent.getItems().remove(item);
        em.persist(ent);
        em.remove(item);
    }

    
    
    @Override
    public void addSubEntityItem(Long parent_id, String value, HashMap<String, String> filling) {
        MetaUnitEntityItem p_item = em.find(MetaUnitEntityItem.class, parent_id);
        MetaUnitEntityItem item = new MetaUnitEntityItem(value);
        item.setMapped_values(filling);
        p_item.getSubitems().add(item);
        em.persist(p_item);
    }

    @Override
    public void updateMetaUnitEntityItemLinks(MetaUnitEntityItemDTO old,MetaUnitEntityItemDTO nev){
        
        MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,old.getId());
        if(item_old != null)
         {
            item_old.setTagged_entities_identifiers(old.getTagged_entities_identifiers());
            item_old.setTagged_entities_ids(old.getTagged_entities_ids());
            em.persist(item_old);

            MetaUnitEntityItem item_new = em.find(MetaUnitEntityItem.class,nev.getId());
            item_new.setTagged_entities_identifiers(nev.getTagged_entities_identifiers());
            item_new.setTagged_entities_ids(nev.getTagged_entities_ids());
            em.persist(item_new);
         }
    }

    @Override
    public void updateMetaUnitEntityItemLinks(MetaUnitEntityItemDTO dto) {
         MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,dto.getId());
         if(item_old != null)
         {
            item_old.setTagged_entities_identifiers(dto.getTagged_entities_identifiers());
            item_old.setTagged_entities_ids(dto.getTagged_entities_ids());
            em.persist(item_old);
         }
        
    }

    @Override
    public void updateMetaUnitEntityItemLinks(Long item_id,ArrayList<Long> tagged_ids, String identifier) {
        MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,item_id);
         if(item_old != null)
         {
            ArrayList<String> new_tagged_idents = new ArrayList<String>();
            ArrayList<Long> new_tagged_ids = new ArrayList<Long>();
            int i = 0;
            if(item_old.getTagged_entities_identifiers() !=null)
            for(String ident:item_old.getTagged_entities_identifiers())
            {
                if(!ident.equals(identifier))
                {
                    new_tagged_idents.add(ident);
                    new_tagged_ids.add(item_old.getTagged_entities_ids().get(i));
                }
                i++;
            }
            for(Long id:tagged_ids)
            {
                 new_tagged_idents.add(identifier);
                 new_tagged_ids.add(id);
            }
            item_old.setTagged_entities_identifiers(new_tagged_idents);
            item_old.setTagged_entities_ids(new_tagged_ids);
            em.persist(item_old);
         }
    }

    @Override
    public void setStartupContent(StartupBundleDTO dto) {
        DatabankStartPage d = DatabankStartPage.getStartPageSingleton(em);
        d.updateFromDTO(em, dto);
        em.persist(d);
    }
    
    
     @Override
    public ArrayList<VarDTO_Light> findVarsLikeThis(Long var_id, ComparativeSearchParamsDTO params) {
         Var v = em.find(Var.class, var_id);
         v.setEM(em);
         VarDTO_Detailed dto =  v.toDTO_Detailed(null,em); 
         ArrayList<Long> varids = doSearchLikewise(dto, params);
         ArrayList<VarDTO_Light> lst = new ArrayList<VarDTO_Light>();
         if(varids.size()>0) lst = user_bean.getVarDTOs(varids);
         return lst;
     }
     private ArrayList<String> cropSearchString(String str, int granularity)
     {
        ArrayList<String> strs = new ArrayList<String>();
        
        String [] arr = str.split(" "); 
        for(int i = 0; i < arr.length;i+=granularity)
        {
            StringBuilder strb = new StringBuilder();
            for(int j = i; j < i+granularity && j < arr.length;j++)
            {
                 strb.append(arr[j]);
                 strb.append(" ");
            }
            strs.add(strb.toString());
        }
         //str.
         return strs;
     }
     private String constructSearchLikewiseQuery(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params)
     {
        //here we compose first likewise search query
         // simplest form - search for question
         
         String question_label = origin_var.getLabel();
         ArrayList<String> sublabels = cropSearchString(question_label, 3);
         
         String query = "";
        
        
        
        //JSONObject obj_bool = new JSONObject();
        //JSONObject obj_must = new JSONObject();
        //JSONArray arr_must = new JSONArray();

//        JsonObjectNodeBuilder builder = anObjectBuilder()
//        .withField("name", aStringBuilder("Black Lace"))
//        .withField("sales", aNumberBuilder("110921"))
//        .withField("totalRoyalties", aNumberBuilder("10223.82"))
//        .withField("singles", anArrayBuilder()
//                .withElement(aStringBuilder("Superman"))
//                .withElement(aStringBuilder("Agadoo"))
//        );
        
       
        
        JsonArrayNodeBuilder arr_contains = anArrayBuilder();

        //JSONObject obj_bool_contains_too = new JSONObject();
        //JSONObject obj_contains_too = new JSONObject();
        //JSONArray arr_contains_too = new JSONArray();



        int index_c=0,index_c2=0,index_c3=0;
        for(int i = 0; i < sublabels.size();i++)
        {
            JsonObjectNodeBuilder obj_b = anObjectBuilder()
                .withField("text_phrase", anObjectBuilder()
                    .withField("sociovar_name", anObjectBuilder()
                        .withField("query", aStringBuilder(sublabels.get(i)))
                     )
               //     .withField("fuziness", aNumberBuilder("0.2"))
                 );
               // obj_b.put("text_phrase", obj_field_name);
                arr_contains.withElement(obj_b);
        }
        
        JsonObjectNodeBuilder obj_bool_contains = anObjectBuilder()
                .withField("bool", anObjectBuilder()
                    .withField("should", arr_contains)
                );
        
        //JSONObject obj_bool_contains = new JSONObject();
        //JsonObjectNodeBuilder obj_contains = anObjectBuilder();
        

      
        //obj_contains_too.put("should", arr_contains_too);
        //obj_bool_contains_too.put("bool", obj_contains_too);

       
        //if(arr_contains.size()>0)arr_must.set(0, obj_bool_contains);
        //if(arr_contains_too.size()>0)arr_must.set(1, obj_bool_contains_too);
       
        //obj_must.put("must", arr_must);
        //obj_bool.put("bool", obj_must);
//	      String quer = obj_bool.toString();
         JsonRootNode json = obj_bool_contains.build();
         String str = json.toString();
         query = JSON_FORMATTER.format(json);
         //query = query.replaceAll("\\<.*?>","");
         //query = query.replaceAll("<[^>]+>", "");
         
	 //query = json.toString();
         return query;
     }
     
     private ArrayList<Long> doParseLikewiseSearchResult(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params,String result)
     {  
         ArrayList<Long> var_ids = new ArrayList<Long>();
        
        try {
            JsonRootNode res = JDOM_PARSER.parse(result);
            JsonNode hits = res.getNode("hits");
            List<JsonNode> hiters = hits.getArrayNode("hits");
            
            //JSONObject res = JSONObject.fromObject(result);
           // JSONObject res = (JSONObject)JSONParser.parse(result);
            //JSONObject hits = (JSONObject)res.get("hits");
            //JSONObject total = hits.get("total");
            
            BigDecimal totalRoyalties = asBigDecimal(hits.getNumberValue("total"));
            Integer tot = totalRoyalties.intValue();
            
            //JSONArray hits_arr = (JSONArray)hits.getJSONArray("hits");
           // if(hiters.isEmpty())
           // {
                        //display.getCenterPanel().clear();
                        //display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
                    //return;
           // }
            for (int i = 0; i < hiters.size(); i++)
            {
                JsonNode hit = (JsonNode)hiters.get(i);
                BigDecimal varid = asBigDecimal(hit.getNode("_source").getNumberValue("sociovar_ID"));
                Long ivarid = varid.longValue();
                var_ids.add(ivarid);
            }

            
        } catch (InvalidSyntaxException ex) {
            Logger.getLogger(AdminSocioResearchSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var_ids;
     }
     
     private ArrayList<Long> doSearchLikewise(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params)
     {
        String query = constructSearchLikewiseQuery(origin_var, params); 
        String [] types = new String[]{"sociovar"};
        String result = user_bean.doIndexSearch(query, types);
        ArrayList<Long> var_ids = doParseLikewiseSearchResult(origin_var, params, result);
        return var_ids;   
     }

    @Override
    public void generalizeVars(ArrayList<Long> gen_var_ids) {
        
        
         try {
             for(Long var_id:gen_var_ids)
             {
                Var dsVar = em.find(Var.class, var_id);
                ArrayList<Long> other = new ArrayList<Long>();
                for(Long vid:gen_var_ids)if(vid.equals(var_id))other.add(vid);
                dsVar.setGeneralized_var_ids(other);
                em.persist(dsVar);
             }
	    } finally {
	    }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
