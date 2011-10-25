package com.heroku.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.facebook.api.Checkin;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class HomeServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signed_request = req.getParameter("signed_request");
		
		if(signed_request != null) {
			String[] elements = signed_request.split("\\.");
			
			if(elements.length > 1) {			
				String payload = elements[1];
				Base64 decoder = new Base64(true);
				String data = new String(decoder.decode(payload.getBytes()));
				
				req.setAttribute("oauth", getOAuthToken(data));
				String accessToken = getOAuthToken(data);
				if(accessToken == null || "".equals(accessToken)) {
					req.setAttribute("sendRedirect", true);
				} else {
					req.setAttribute("sendRedirect", false);					
					req.setAttribute("checkIns", getCheckIns(req, getOAuthToken(data)));
				}
			} else {
				req.setAttribute("sendRedirect", true);
			}
		} else {
			req.setAttribute("sendRedirect", true);
		}
		
	    req.getRequestDispatcher("canvas-social.jsp").forward(req, resp);

	}
	
	private String getOAuthToken(String data) throws ServletException {
		ObjectMapper mapper = new ObjectMapper();
		String oauthToken = null;
		try {
			JsonNode rootNode = mapper.readValue(data.getBytes(), JsonNode.class);
			if(rootNode.path("oauth_token") != null) {				
				oauthToken = rootNode.path("oauth_token").getTextValue();
			}
		} catch (JsonParseException e) {
			throw new ServletException(e);
		} catch (JsonMappingException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		return oauthToken;
	}
    
    private List<Checkin> getCheckIns(HttpServletRequest req, String token) {
    	Facebook facebook = new FacebookTemplate(token);
    	List<Checkin> checkIns = facebook.placesOperations().getCheckins();
    	
    	return checkIns;
    }

}
