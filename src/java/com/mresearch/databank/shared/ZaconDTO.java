package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ZaconDTO extends ZaconDTO_Light{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2443613870378329241L;
	private String contents = "";
	private HashMap<String,String> filling;
//	private ArrayList<String> key_words,authors;
//	private Date date,accept_date,decline_date;
	private String json_desctiptor;
        	private Long file_accessor_id;
	public Long getFile_accessor_id() {
		return file_accessor_id;
	}

	public void setFile_accessor_id(Long file_accessor_id) {
		this.file_accessor_id = file_accessor_id;
	}
	public String getJson_desctiptor() {
		return json_desctiptor;
	}


	public void setJson_desctiptor(String json_desctiptor) {
		this.json_desctiptor = json_desctiptor;
	}
	private Long enclosure_key;
	public ZaconDTO(){}
        static int CONTENTS_SHORTAGE_NUMBER = 350;
	public ZaconDTO(Long id,String header, String contents,Long encl_k,HashMap<String,String> fill)
	{
		super(id,header);
		int end = contents.length() > CONTENTS_SHORTAGE_NUMBER? CONTENTS_SHORTAGE_NUMBER:contents.length();
		
                this.contents = contents.substring(0,end);
                this.enclosure_key = encl_k;
                this.filling = fill;
                
                //this.setText(dto.getCode()+": "+dto.getLabel());
		//l.setText(dto.getCode()+": "+dto.getLabel().substring(0, end));
	}
        public ZaconDTO(long id,String header, String contents)
	{
		super(id,header);
		this.contents = contents;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getEnclosure_key() {
		return enclosure_key;
	}
	public void setEnclosure_key(Long enclosure_key) {
		this.enclosure_key = enclosure_key;
	}
	
	
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
//	public Date getDecline_date() {
//		return decline_date;
//	}
//	public void setDecline_date(Date decline_date) {
//		this.decline_date = decline_date;
//	}
//	public Date getAccept_date() {
//		return accept_date;
//	}
//	public void setAccept_date(Date accept_date) {
//		this.accept_date = accept_date;
//	}
//	public ArrayList<String> getKey_words() {
//		return key_words;
//	}
//	public void setKey_words(ArrayList<String> key_words) {
//		this.key_words = key_words;
//	}
//	public ArrayList<String> getAuthors() {
//		return authors;
//	}
//	public void setAuthors(ArrayList<String> authors) {
//		this.authors = authors;
//	}
	public HashMap<String,String> getFilling() {
		return filling;
	}
	public void setFilling(HashMap<String,String> filling) {
		this.filling = filling;
	}
}
