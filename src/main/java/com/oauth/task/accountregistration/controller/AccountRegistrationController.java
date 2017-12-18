/**
 * 
 */
package com.oauth.task.accountregistration.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.oauth.task.accountregistration.AccountRegistrationConstants;
import com.oauth.task.accountregistration.exception.DataFormatException;
import com.oauth.task.accountregistration.util.AccountRegistrationUtil;

/**
 * @author SUSHREE
 *
 */
@RestController
public class AccountRegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRegistrationController.class);
	
	@Autowired
	AccountRegistrationUtil accRegUtil;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@PostMapping(path = "/registerAccount", consumes = "application/json", produces = "application/json")
	
	public ResponseEntity<?> registerAccount() {
		RestTemplate rt = new RestTemplate();
		
		Map<String, String> sessionInfo = accRegUtil.getAccountRegistrationSessionInfo();	
		LOGGER.info("Returned session info from Util class");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Cookie", sessionInfo.get(AccountRegistrationConstants.COOKIE_VALUE));

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add(AccountRegistrationConstants.FORM_KEY, sessionInfo.get(AccountRegistrationConstants.FORM_KEY));
		map.add("firstname", "FN9");
		map.add("lastname", "LN9");
		map.add("email", "ea9@email.com");
		map.add("password", "test12");
		map.add("confirmation", "test12");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		
		ResponseEntity<String> response = rt.postForEntity( "http://54.144.243.81/index.php/customer/account/createpost/", request , String.class );
//		ResponseEntity<String> response = rt.exchange("http://54.144.243.81/index.php/customer/account/index/", HttpMethod.GET, request, String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		
		if (validateResponse(response, sessionInfo)) {
			throw new DataFormatException("email id is not in correct format");
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public boolean validateResponse(ResponseEntity<String> response, Map<String, String> sessionInfo) {
		
		URI locationUrl = response.getHeaders().getLocation();
		RestTemplate rt = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Cookie", sessionInfo.get(AccountRegistrationConstants.COOKIE_VALUE));
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add(AccountRegistrationConstants.FORM_KEY, sessionInfo.get(AccountRegistrationConstants.FORM_KEY));
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		HttpEntity requestEntity = new HttpEntity(request);
//		ResponseEntity<String> getResponse = rt.exchange(locationUrl.toString(), HttpMethod.GET, requestEntity , String.class );
		/*Iterator<Entry<String, List<String>>> itr = httpHeaders.entrySet().iterator();
		List<String> cookieVal = new ArrayList<>();
		while (itr.hasNext()) {
			Entry<String, List<String>> iterElement = itr.next();
			String cookieName = iterElement.getKey();
			if ("Set-Cookie".equalsIgnoreCase(cookieName)) {
				cookieVal = iterElement.getValue();
				break;
			}
			
		}
		String cookieFrontEnd = null;
		for (String cookieName : cookieVal) {
			if (cookieName.contains("persistent_shopping_cart")) {
				cookieFrontEnd = cookieName.substring(cookieName.indexOf("frontend=")+9, cookieName.indexOf(";"));
				break;
			}
		}*/
		UriComponentsBuilder builder = UriComponentsBuilder
			    .fromUriString(locationUrl.toString())
			    // Add query parameter
			    .queryParam(AccountRegistrationConstants.FORM_KEY, sessionInfo.get(AccountRegistrationConstants.FORM_KEY));

			RestTemplate restTemplate = new RestTemplate();
			/*String response1 = restTemplate.getForObject(builder.toUriString(), String.class);*/
		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpEntity<String> response1 = restTemplate.exchange(
		        builder.build().encode().toUri(), 
		        HttpMethod.GET, 
		        entity, 
		        String.class);
		System.out.println(response1.getBody());
		Document htmlDoc = Jsoup.parse(response1.getBody());
		Elements formKey = htmlDoc.select("li[class*=error-msg]");//doc.select("h6.uiStreamMessage > span.messageBody")
//		System.out.println(getResponse.getBody());
		return Boolean.TRUE;
	}
	
}
