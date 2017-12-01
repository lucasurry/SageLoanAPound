package com.fairsail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/creditsites")
public class CreditScoreService {
	
	CreditScoreDao creditScoreDao = new CreditScoreDao();
	
	@GET
	@Path("/creditsite/credit-scores/{userName}")
	@Produces(MediaType.APPLICATION_XML)
	public CreditScore getCreditScore(@PathParam("userName") String userName){
		return creditScoreDao.getCreditScore(userName);
	}
	
	@GET
	@Path("/creditsite2/credit-scores/{userName}")
	@Produces(MediaType.APPLICATION_XML)
	public CreditScore getCreditScore2(@PathParam("userName") String userName){
		return creditScoreDao.getCreditScore2(userName);
	}
}
