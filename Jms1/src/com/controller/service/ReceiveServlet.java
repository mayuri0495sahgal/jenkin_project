package com.controller.service;

import java.io.IOException;
import java.io.PrintWriter;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ReceiveServlet
 */
public class ReceiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		try {
			String ss=request.getParameter("message");
			Context iniCtx=new InitialContext();
			Queue que=(Queue) iniCtx.lookup("java:/Zensar_queue");
			Destination dest=(Destination) que;
			
			QueueConnectionFactory qcf=(QueueConnectionFactory) iniCtx.lookup("java:/ConnectionFactory");//step1
			connection = qcf.createConnection();
			connection=qcf.createConnection();//step 2
			Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//step 3
			MessageConsumer consumer=session.createConsumer(dest);//step 4
			connection.start();//delivery will start
			//TextMessage message=session.createTextMessage(ss);//step 5
			//System.out.println("sending message: "+message.getText());
			//producer.send(message);//step 6
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<html><body>");
			while(true)
			{
			
			Message m=consumer.receive(1);
			if(m!=null)
			{
				if(m instanceof TextMessage)
				{
				TextMessage message=(TextMessage)m;
				out.println("Reading message: "+message.getText());
			    }
			}
				else
				{
					break;
				}
			}
			
			out.println("to receive message please <a href=jms_html.html>CLICK HERE </a>");
			out.println("</html></body>");
			
		
		} 
			catch (Exception e) 
		{
			
			e.printStackTrace();
		}
finally {
			
			try {
				connection.close();
			} 
			catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		
	}

}
