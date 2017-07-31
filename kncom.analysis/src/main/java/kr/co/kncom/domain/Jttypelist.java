package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Jttypelist {
	
	@Id
	private int ind;
	private String address;
	private int sidogus_ind;
	private int dongs_ind;
	private int landcode;
	private int bunji1;
	private int bunji2;
	private String permissionpk;
	private String name;
	private String specialname;
	private String block;
	private String rote;
	private int gubuncode;
	private String gubunname;
	private float area;
	private int household;
	private int createday;
	
}
