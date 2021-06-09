
package com.spantag.socialMediaAppln.utils;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class QuoteDBProcedureHelper {

	@Autowired
	EntityManager em;

	private static final Logger logger = LoggerFactory.getLogger(QuoteDBProcedureHelper.class);

	public LinkedHashMap<String, String> callCreateCustomerProcedure(int year) throws SQLException {

		LinkedHashMap<String, String> returnValue = new LinkedHashMap<String, String>();

		// define the stored procedure
		StoredProcedureQuery query = em.createStoredProcedureQuery("myProcedure");
		query.registerStoredProcedureParameter("need_year", int.class, ParameterMode.IN);
		
		// set input parameter
		query.setParameter("need_year", year);

		logger.debug("Customer creation: " + "Before DB Procedure Call");
		// call the stored procedure and get the result

		Instant start = Instant.now();
		/* ... the code being measured starts ... */

		query.execute();

		Instant end = Instant.now();

		Duration interval = Duration.between(start, end);

		System.out.println("Call_Customer_Create_Procedure------------------Execution time in seconds: -----------------------" + interval.getSeconds());

		logger.debug("Customer creation: " + "After DB Procedure Call");
		String P_OUT_FLAG = "crewar";

		
		// Throws SQL Exception in failed Case----
		if (P_OUT_FLAG.equalsIgnoreCase("1")) {
			throw new SQLException(P_OUT_FLAG.toString());
		}

		returnValue.put("flag", P_OUT_FLAG);
		
		return returnValue;

	}


	public LinkedHashMap<String, String> callQuoteGenerationProcedure(String lobcode,String productCode, Long quotSysId) throws SQLException {

		/*public LinkedHashMap<String, String> callQuoteGenerationProcedure(String productCode, Long quotSysId) throws SQLException {

	public LinkedHashMap<String, String> callQuoteGenerationProcedure(String lobCode,String productCode, Long quotSysId) throws SQLException {
*/

		String intgType = "01";
		String tranType = "01";
		String recType = "NULL";

		LinkedHashMap<String, String> returnValue = new LinkedHashMap<String, String>();

		// define the stored procedure
		StoredProcedureQuery query = em.createStoredProcedureQuery("PKG_INTEGRATION_MAPPING.PR_GEN_INTG_DATA");
		query.registerStoredProcedureParameter("P_INTG_TYPE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_MODU_TYPE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_REC_TYPE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_CLASS_CODE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_PROD_CODE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_PARA_1", Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_PARA_2", Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_OUT_FLAG", String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_MSG", String.class, ParameterMode.OUT);

		// set input parameter
		query.setParameter("P_INTG_TYPE", intgType);
		query.setParameter("P_MODU_TYPE", tranType);
		query.setParameter("P_REC_TYPE", recType);
		query.setParameter("P_CLASS_CODE", lobcode);
		query.setParameter("P_PROD_CODE", productCode);
		query.setParameter("P_PARA_1", quotSysId);
		query.setParameter("P_PARA_2", 0L);

		logger.debug("Quuote creation: " + "Before DB Procedure Call");
		// call the stored procedure and get the result
		Instant start = Instant.now();
		/* ... the code being measured starts ... */

		query.execute();

		Instant end = Instant.now();

		Duration interval = Duration.between(start, end);

		System.out.println("Call_Quote_Create_Procedure------------------Execution time in seconds: -----------------------" + interval.getSeconds());

		logger.debug("Quuote creation: " + "After DB Procedure Call");
		
		String P_OUT_FLAG = null;
		/*P_OUT_FLAG = query.getOutputParameterValue("P_OUT_FLAG") != null ? null : query.getOutputParameterValue("P_OUT_FLAG").toString();
		System.out.println("Flag"+P_OUT_FLAG);*/

		String P_OUT_MSG = null;
		P_OUT_MSG = (String) query.getOutputParameterValue("P_OUT_MSG");
		
		/*if (P_OUT_FLAG.equalsIgnoreCase("1")) {
			throw new SQLException(P_OUT_MSG.toString());
		}
		returnValue.put("flag", P_OUT_FLAG);
		returnValue.put("message", P_OUT_MSG);*/
		
		returnValue.put("flag", P_OUT_FLAG);
		returnValue.put("message", "Success");
		return returnValue;

	}

	public LinkedHashMap<String, String> callPolicyApprovalProcedure(Long quoteSysId) throws SQLException {

		LinkedHashMap<String, String> returnValue = new LinkedHashMap<String, String>();

		// define the stored procedure
		StoredProcedureQuery query = em.createStoredProcedureQuery("PKG_INTEGRATION_MAPPING.PR_SERVICE_QUOT_CONV_POL");
		query.registerStoredProcedureParameter("P_INTG_TYPE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_MODU_TYPE", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_POL_SYS_ID", Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_POL_END_NO_IDX", Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_POL_END_SR_NO", Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_OUT_FLAG", Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_MSG", String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_POL_SYS_ID", Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_POL_NO", String.class, ParameterMode.OUT);
		/*query.registerStoredProcedureParameter("P_OUT_CUST_NAME", String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_CUST_MOBNO", String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter("P_OUT_POL_PERIOD", String.class, ParameterMode.OUT);*/

		String intgType = "01";
		String tranType = "01";
		Long polEndNo = (long) 0;
		Long polEndSrNo = (long) 0;

		// set input parameter
		query.setParameter("P_INTG_TYPE", intgType);
		query.setParameter("P_MODU_TYPE", tranType);
		query.setParameter("P_POL_SYS_ID", quoteSysId);
		query.setParameter("P_POL_END_NO_IDX", polEndNo);
		query.setParameter("P_POL_END_SR_NO", polEndSrNo);

		// call the stored procedure and get the result
		Instant start = Instant.now();
		/* ... the code being measured starts ... */

		query.execute();

		Instant end = Instant.now();

		Duration interval = Duration.between(start, end);

		System.out.println("Call_Policy_Approval_Procedure------------------Execution time in seconds: -----------------------" + interval.getSeconds());


		if (query.getOutputParameterValue("P_OUT_FLAG").toString().equalsIgnoreCase("1")) {
			String P_OUT_MSG = query.getOutputParameterValue("P_OUT_MSG").toString();
			throw new SQLException(P_OUT_MSG.toString());
			// returnValue.put("flag", P_OUT_FLAG);
			// returnValue.put("message", P_OUT_MSG);
		}
		else {
			
			
			
			String P_OUT_FLAG = query.getOutputParameterValue("P_OUT_FLAG").toString();
			String P_OUT_MSG = query.getOutputParameterValue("P_OUT_MSG").toString();
			String P_OUT_POL_SYS_ID = (query.getOutputParameterValue("P_OUT_POL_SYS_ID") != null) ? query.getOutputParameterValue("P_OUT_POL_SYS_ID").toString() : null;
			String P_OUT_POL_NO = (query.getOutputParameterValue("P_OUT_POL_NO") != null) ? query.getOutputParameterValue("P_OUT_POL_NO").toString() : null;
			
			returnValue.put("flag", P_OUT_FLAG);
			returnValue.put("message", P_OUT_MSG);
			returnValue.put("polSysID", P_OUT_POL_SYS_ID);
			returnValue.put("polNo", P_OUT_POL_NO);
			
			/*String P_OUT_CUST_NAME = (query.getOutputParameterValue("P_OUT_CUST_NAME") != null) ? query.getOutputParameterValue("P_OUT_CUST_NAME").toString() : null;
			String P_OUT_CUST_MOBNO = (query.getOutputParameterValue("P_OUT_CUST_MOBNO") != null) ? query.getOutputParameterValue("P_OUT_CUST_MOBNO").toString() : null;
			String P_OUT_POL_PERIOD = (query.getOutputParameterValue("P_OUT_POL_PERIOD") != null) ? query.getOutputParameterValue("P_OUT_POL_PERIOD").toString() : null;
			returnValue.put("custName", P_OUT_CUST_NAME);
			returnValue.put("mobno", P_OUT_CUST_MOBNO);
			returnValue.put("polPeriod", P_OUT_POL_PERIOD);*/
		}
		return returnValue;

	}

	public LinkedHashMap<String, String> callPolicySchedule(String reportId, String policyNo) throws SQLException {

		LinkedHashMap<String, String> returnValue = new LinkedHashMap<String, String>();

		// define the stored procedure
		StoredProcedureQuery query = em.createStoredProcedureQuery("PROC_GEN_REPORT_BI");
		query.registerStoredProcedureParameter("REP_NAME", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("POLICY_NO", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("REP_OUTPUT", String.class, ParameterMode.OUT);

		// set input parameter
		query.setParameter("REP_NAME", "P11KEN1002");
		query.setParameter("POLICY_NO", policyNo);

		// call the stored procedure and get the result
		Instant start = Instant.now();
		/* ... the code being measured starts ... */

		query.execute();

		Instant end = Instant.now();

		Duration interval = Duration.between(start, end);

		System.out.println("Call_Policy_Schedule_Procedure------------------Execution time in seconds: -----------------------" + interval.getSeconds());

		String REP_OUTPUT = query.getOutputParameterValue("REP_OUTPUT").toString();
		returnValue.put("url", REP_OUTPUT);
		return returnValue;

	}
	
	 

}

