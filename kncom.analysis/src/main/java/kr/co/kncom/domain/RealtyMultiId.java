package kr.co.kncom.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class RealtyMultiId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String step;
	private int si;
	private int gu;
	private int dong;
	private int jibun1;
	private int jibun2;
}
