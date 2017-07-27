package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Sidogus {

	@Id
	private int ind;
	private String name;
	private int si_ind;
	private int onnara_ind;
	@Lob
	private byte[] polygon;
	private String x;
	private String y;
	private int mapv;
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

	public int getSi_ind() {
		return si_ind;
	}

	public void setSi_ind(int si_ind) {
		this.si_ind = si_ind;
	}

	public int getOnnara_ind() {
		return onnara_ind;
	}

	public void setOnnara_ind(int onnara_ind) {
		this.onnara_ind = onnara_ind;
	}

	public byte[] getPolygon() {
		return polygon;
	}

	public void setPolygon(byte[] polygon) {
		this.polygon = polygon;
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

	public int getMapv() {
		return mapv;
	}

	public void setMapv(int mapv) {
		this.mapv = mapv;
	}

	public int getMolit_si_ind() {
		return molit_si_ind;
	}

	public void setMolit_si_ind(int molit_si_ind) {
		this.molit_si_ind = molit_si_ind;
	}

}
