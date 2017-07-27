package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Newdongs {

	@Id
	private int ind;
	private int sigogus_ind;
	private String name;
	private int si_ind;
	@Lob
	private byte[] polygon;
	private String x;
	private String y;
	private int mapv;
	private String nearinds;
	private String neardeepinds;

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	public int getSigogus_ind() {
		return sigogus_ind;
	}

	public void setSigogus_ind(int sigogus_ind) {
		this.sigogus_ind = sigogus_ind;
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

	public String getNearinds() {
		return nearinds;
	}

	public void setNearinds(String nearinds) {
		this.nearinds = nearinds;
	}

	public String getNeardeepinds() {
		return neardeepinds;
	}

	public void setNeardeepinds(String neardeepinds) {
		this.neardeepinds = neardeepinds;
	}

}
