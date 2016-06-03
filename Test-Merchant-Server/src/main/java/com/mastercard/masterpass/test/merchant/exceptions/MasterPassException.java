package com.mastercard.masterpass.test.merchant.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class MasterPassException extends WebApplicationException {
	
	private static final long serialVersionUID = 1L;

	public MasterPassException(Status status, String code, String message) {
		super(Response.status(status)
				.entity(new ErrorResponse(status.toString(), code, message))
				.type(MediaType.APPLICATION_JSON).build());
	}
}
