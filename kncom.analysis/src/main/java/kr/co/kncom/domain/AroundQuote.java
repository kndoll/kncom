package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AroundQuote {
	
	@Id
	private String key;
	private String type;
	private String si;
	private String sreg;
	private String seug;
	private String month;
	private String sedecount;
	private String memeChange;
	private String memCValue;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSi() {
		return si;
	}
	public void setSi(String si) {
		this.si = si;
	}
	public String getSreg() {
		return sreg;
	}
	public void setSreg(String sreg) {
		this.sreg = sreg;
	}
	public String getSeug() {
		return seug;
	}
	public void setSeug(String seug) {
		this.seug = seug;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getSedecount() {
		return sedecount;
	}
	public void setSedecount(String sedecount) {
		this.sedecount = sedecount;
	}
	public String getMemeChange() {
		return memeChange;
	}
	public void setMemeChange(String memeChange) {
		this.memeChange = memeChange;
	}
	public String getMemCValue() {
		return memCValue;
	}
	public void setMemCValue(String memCValue) {
		this.memCValue = memCValue;
	}
	
	
}
