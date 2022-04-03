package com.booking.flights.exceptions;

public enum RestErrorCodes {
	
	ERRORE_VALIDAZIONE(1,"ERR_0002");

	private final int id;
	private final String code;

	RestErrorCodes(int id, String code) {
		this.id = id;
		this.code = code;
	}

	public int getId() {
		return this.id;
	}

	public String getCode() {
		return this.code;
	}

}
