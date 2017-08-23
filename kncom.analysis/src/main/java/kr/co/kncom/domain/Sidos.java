package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sidos {

	@Id
	private int ind;
	private String name;
	private String x;
	private String y;
	private int molit_si_ind;

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public int getMolit_si_ind() {
		return molit_si_ind;
	}

	public void setMolit_si_ind(int molit_si_ind) {
		this.molit_si_ind = molit_si_ind;
	}

}
