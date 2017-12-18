/**
 * 
 */
package com.oauth.task.accountregistration.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.oauth.task.accountregistration.AccountRegistrationConstants;

/**
 * @author SUMAN
 *
 */
@Component("accRegUtil")
public class AccountRegistrationUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRegistrationUtil.class);

	
	public Map<String, String> getAccountRegistrationSessionInfo() {
		
		Map<String, String> sessionInfo = new HashMap<>();
		
		URLConnection connection;
		try {
			// Open URLConnection to the host to get the registration page
			connection = new URL("http://54.144.243.81/index.php/customer/account/create/").openConnection();
			LOGGER.info("Connection established with 54.144.243.81");
			
			// get the Cookie information from the header
			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
			String cookieFrontEnd = null;
			for (String cookieName : cookies) {
				if (cookieName.startsWith(AccountRegistrationConstants.COOKIE_NAME_FRONTEND)) {
					cookieFrontEnd = cookieName.substring(cookieName.indexOf(AccountRegistrationConstants.COOKIE_NAME_FRONTEND), cookieName.indexOf(";"));
					LOGGER.info("Found Cookie with name frontend");
					break;
				}
			}
			
			final InputStream inputStream = connection.getInputStream();
	        final String html = IOUtils.toString(inputStream);
	        Document htmlDoc = Jsoup.parse(html);
			Elements formKey = htmlDoc.select("input[name=form_key]");
			String formKeyValue = formKey.get(0).attr("value");
	        IOUtils.closeQuietly(inputStream);
	        
	        sessionInfo.put(AccountRegistrationConstants.FORM_KEY, formKeyValue);
			sessionInfo.put(AccountRegistrationConstants.COOKIE_VALUE, cookieFrontEnd);
			LOGGER.info("form_Key: "+formKeyValue+", cookieValue: "+cookieFrontEnd);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sessionInfo;
	}
}
