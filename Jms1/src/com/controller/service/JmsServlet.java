package com.controller.service;

import java.io.IOException;
import java.io.PrintWriter;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JmsServlet
 */
public class JmsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String ss=request.getParameter("m");
		Connection connection =null;
		try {
			Context iniCtx=new InitialContext();
			Queue que=(Queue) iniCtx.lookup("java:/Zensar_queue");
			Destination dest=(Destination) que;
			
			QueueConnectionFactory qcf=(QueueConnectionFactory) iniCtx.lookup("java:/ConnectionFactory");//step1
			connection = qcf.createConnection();//step2
			Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//step 3
			MessageProducer producer=session.createProducer(dest);//step 4 push message inside des
			
			TextMessage message=session.createTextMessage(ss);//step 5
			System.out.println("sending message: "+message.getText());
			producer.send(message);//step 6
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<html><body>");
			out.println("message "+message.getText()+"sent successfully");
			out.println("to receive message please <a href=ReceiveServlet>CLICK HERE </a>");
			out.println("</html></body>");
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
		
		
	}


