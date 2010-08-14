package org.openxdata.forms.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.openxdata.forms.server.util.RedirectUtil;


/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class DataUploadServlet extends HttpServlet{

	public static final long serialVersionUID = 233456789;


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//try{Thread.sleep(2000);}catch(Exception ex){}

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String redirectUrl = request.getParameter("redirectUrl");

		if(redirectUrl != null)
			RedirectUtil.doPost(username, password, redirectUrl, request, response);
		else{
			if(username.equals("test") || password.equals("test")){
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

			String xml = IOUtils.toString(request.getInputStream(),"UTF-8");
			//System.out.println(xml);
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}
}
