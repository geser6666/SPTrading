package com.trading.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.R.integer;
import android.R.string;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.preference.PreferenceManager;
import android.text.InputFilter.LengthFilter;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Xml;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import com.trading.activity.OrderActivity;
import com.trading.services.GPSService;
import com.trading.utils.*;
import com.trading.activity.MainActivity;
import com.trading.activity.SyncActivity;
import com.trading.dao.ClientCardsDao;
import com.trading.dao.OrdersDao;

import static android.support.v4.app.ActivityCompat.startActivity;

public class DBInteraction {
	private String urlString;
	private Integer m_id;
	private Integer prop;
/*
	public DBInteraction(Integer manager_id, Integer property) {
		m_id = manager_id;
		prop = property;
		// urlString="http://192.168.251.1:8080/getxml.php";
		//urlString = "http://85.90.210.161/testtrading/";
		//urlString = "http://trading.dtr.org.ua/";
		urlString = "http://85.90.210.161/trading/";
	
	}
*/
	public DBInteraction(Integer manager_id, Integer property, Context context) {
		m_id = manager_id;
		prop = property;
		// urlString="http://192.168.251.1:8080/getxml.php";
		//urlString = "http://85.90.210.161/testtrading/";
		//urlString = "http://trading.dtr.org.ua/";
		//urlString = "http://85.90.210.161/trading/";
		urlString =PreferenceManager.getDefaultSharedPreferences(context).getString("server", "Сервер не задан!!Обновлений не будет!!!");
	}
	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;
	//	System.setProperty("http.keepAlive", "false");
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		String data = "manager_id=" + URLEncoder.encode(m_id.toString())
				+ "&prop=" + URLEncoder.encode(prop.toString());
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded");
//		conn.setRequestProperty("Content-length", Integer.toString(data.length()));
		try {
			PrintStream out = new PrintStream(conn.getOutputStream());
			out.println(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Не HTTP соединение");
		try {

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			// httpConn.setRequestMethod("POST");
			// httpConn.setAllowUserInteraction(false);
			// httpConn.setInstanceFollowRedirects(true);


			httpConn.connect();

			response = httpConn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Ошибка соединения.");
		}
		return in;
	}

	public ArrayList<Partner> GetPartners() throws IOException{
		InputStream in = null;
		ArrayList<Partner> arr = new ArrayList<Partner>();
		try {
			in = OpenHttpConnection(urlString + "getpartners.php");
			  
			// /////
/*
			
			  InputStreamReader isr = new InputStreamReader(in); int charRead;
			  String str=""; char[] inputBuffer=new char[2000];
			  
			  try{ while ((charRead=isr.read(inputBuffer))>0){ String
			  readString=String.copyValueOf(inputBuffer,0,charRead);
			  str+=readString; inputBuffer=new char[2000]; } in.close(); }catch
			  (IOException ex2){ ex2.printStackTrace();
			  
			  }
	*/		 
			// /////
			
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(in);
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			} catch (SAXException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			doc.getDocumentElement().normalize();
			NodeList parNodes = doc.getElementsByTagName("partner");

			for (int i = 0; i < parNodes.getLength(); i++) {
				Node parNode = parNodes.item(i);

				if (parNode.getNodeType() == Node.ELEMENT_NODE) {
					Element parElement = (Element) parNode;
					// id
					NodeList idNodes = (parElement).getElementsByTagName("_id");
					Element idElement = (Element) idNodes.item(0);
					// ---get all the child nodes under the <title> element---
					NodeList textNodes = idElement.getChildNodes();
					Integer id = Integer.parseInt(textNodes.item(0)
							.getNodeValue().toString());

					// name
					NodeList nameNodes = (parElement)
							.getElementsByTagName("name");
					Element nameElement = (Element) nameNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = nameElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String name = "";
					if (textNodes.getLength() > 0)
						name = textNodes.item(0).getNodeValue()
								.toString();
					// name_l
					NodeList name_lNodes = (parElement)
							.getElementsByTagName("name_l");
					Element name_lElement = (Element) name_lNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = name_lElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String name_l = "";
					if (textNodes.getLength() > 0)
						name_l = textNodes.item(0).getNodeValue()
								.toString();

					// address
					NodeList addressNodes = (parElement)
							.getElementsByTagName("address");
					Element addressElement = (Element) addressNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = addressElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String address = "";
					if (textNodes.getLength() > 0)
						address = textNodes.item(0).getNodeValue();

					// phone
					NodeList phoneNodes = (parElement)
							.getElementsByTagName("phone");
					Element phoneElement = (Element) phoneNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = phoneElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String phone = "";
					if (textNodes.getLength() > 0)
						phone = textNodes.item(0).getNodeValue();

					// cat
					NodeList catNodes = (parElement)
							.getElementsByTagName("cat");
					Element catElement = (Element) catNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = catElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String cat = "";
					if (textNodes.getLength() > 0)
						cat = textNodes.item(0).getNodeValue();

					String idhenkel="";
					
					// idhenkel
					NodeList idhenkelNodes = (parElement)
							.getElementsByTagName("idhenkel");
					Element idhenkelElement = (Element) idhenkelNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = idhenkelElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					
					if (textNodes.getLength() > 0)
						idhenkel = textNodes.item(0).getNodeValue();
					
					// week_day
					NodeList week_dayNodes = (parElement).getElementsByTagName("week_day");
					Element week_dayElement = (Element) week_dayNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = week_dayElement.getChildNodes();
					Integer week_day = 0;
			          if (textNodes.getLength() > 0)
					week_day = Integer.parseInt(textNodes.item(0)
							.getNodeValue().toString());

					
					
					
					
					// arr.add(new Partner(id, name, address, phone, 0, 0.0,2));
						arr.add(new Partner(id, name,name_l, address, phone, 0,
										0.0, 0, cat, idhenkel, week_day));
				}

			}

		} catch (IOException e) {
			throw new IOException("Ошибка соединения.");
		}
		return arr;
	}

	public ArrayList<Tovar> GetTovari() throws IOException{
		InputStream in = null;
		ArrayList<Tovar> arr = new ArrayList<Tovar>();
		try {
			in = OpenHttpConnection(urlString + "gettovari.php");
					
			  /*InputStreamReader isr = new InputStreamReader(in); int
			  charRead; String str=""; char[] inputBuffer=new
			  char[2000];
			  
			  try{ while ((charRead=isr.read(inputBuffer))>0){ String
			  readString=String.copyValueOf(inputBuffer,0,charRead);
			  str+=readString; inputBuffer=new char[2000]; }
			  in.close(); }catch (IOException ex2){
			  ex2.printStackTrace();
			  
			  }
			*/
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(in);
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			} catch (SAXException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			doc.getDocumentElement().normalize();
			NodeList tovariNodes = doc.getElementsByTagName("tovar");

			for (int i = 0; i < tovariNodes.getLength(); i++) {
				Node tovarNode = tovariNodes.item(i);

				if (tovarNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tovarElement = (Element) tovarNode;
					// id
					NodeList idNodes = (tovarElement)
							.getElementsByTagName("_id");
					Element idElement = (Element) idNodes.item(0);
					NodeList textNodes = idElement.getChildNodes();
					Integer id = Integer.parseInt(textNodes.item(0)
							.getNodeValue().toString());

					// name
					NodeList nameNodes = (tovarElement)
							.getElementsByTagName("name");
					Element nameElement = (Element) nameNodes.item(0);
					textNodes = nameElement.getChildNodes();
					String name = "";
					if (textNodes.getLength() > 0)
						name = textNodes.item(0).getNodeValue()
								.toString();

					// name_l
					NodeList name_lNodes = (tovarElement)
							.getElementsByTagName("name_l");
					Element name_lElement = (Element) name_lNodes.item(0);
					textNodes = name_lElement.getChildNodes();
					String name_l = "";
					if (textNodes.getLength() > 0)
						name_l = textNodes.item(0).getNodeValue()
								.toString();
					// ed_izm
					NodeList ed_izmNodes = (tovarElement)
							.getElementsByTagName("ed_izm");
					Element ed_izmElement = (Element) ed_izmNodes.item(0);
					// ---get all the child nodes under the <title> element---
					textNodes = ed_izmElement.getChildNodes();
					// ---retrieve the text of the <name> element---
					String ed_izm = "";
					if (textNodes.getLength() > 0)
						ed_izm = textNodes.item(0).getNodeValue();

					// cena
					NodeList cenaNodes = (tovarElement)
							.getElementsByTagName("cena");
					Element cenaElement = (Element) cenaNodes.item(0);
					textNodes = cenaElement.getChildNodes();
					Double cena = 0.0;
					if (textNodes.getLength() > 0)
						cena = Double.valueOf(textNodes.item(0)
								.getNodeValue());

					// faktost
					NodeList faktostNodes = (tovarElement)
							.getElementsByTagName("faktost");
					Element faktostElement = (Element) faktostNodes.item(0);
					textNodes = faktostElement.getChildNodes();
					Double faktost = 0.0;
					if (textNodes.getLength() > 0)
						faktost = Double.valueOf(textNodes.item(0)
								.getNodeValue());

					// dost
					NodeList dostNodes = (tovarElement)
							.getElementsByTagName("dost");
					Element dostElement = (Element) dostNodes.item(0);
					textNodes = dostElement.getChildNodes();
					Double dost = 0.0;
					if (textNodes.getLength() > 0)
						dost = Double.valueOf(textNodes.item(0)
								.getNodeValue());

					// parentnid
					NodeList parentnidNodes = (tovarElement)
							.getElementsByTagName("parentnid");
					Element parentnidElement = (Element) parentnidNodes.item(0);
					textNodes = parentnidElement.getChildNodes();
					// Integer parentnid =0.0;
					// if (textNodes.getLength()>0)
					Integer parentnid = Integer.valueOf(textNodes
							.item(0).getNodeValue());
					// seb
					NodeList sebNodes = (tovarElement)
							.getElementsByTagName("seb");
					Element sebElement = (Element) sebNodes.item(0);
					textNodes = sebElement.getChildNodes();
					Double seb = Double.valueOf(textNodes
							.item(0).getNodeValue());
					// skidka1
					NodeList skidka1Nodes = (tovarElement)
							.getElementsByTagName("skidka1");
					Element skidka1Element = (Element) skidka1Nodes.item(0);
					textNodes = skidka1Element.getChildNodes();
					Double skidka1 = 0.0;
					if (textNodes.getLength() > 0)
						skidka1 = Double.valueOf(textNodes.item(0)
								.getNodeValue());
					// skidka2
					NodeList skidka2Nodes = (tovarElement)
							.getElementsByTagName("skidka2");
					Element skidka2Element = (Element) skidka2Nodes.item(0);
					textNodes = skidka2Element.getChildNodes();
					Double skidka2 = 0.0;
					if (textNodes.getLength() > 0)
						skidka2 = Double.valueOf(textNodes.item(0)
								.getNodeValue());					

					arr.add(new Tovar(id, name,name_l, ed_izm, cena, dost, faktost,
							parentnid, skidka1 , skidka2 , cena,  seb));
				}

			}

		} catch (IOException e) {
			throw new IOException("Ошибка соединения.");
		}
		return arr;
	}

	public ArrayList<Grsklad> GetGroups()
	throws IOException
	{
		InputStream in = null;
		ArrayList<Grsklad> arr = new ArrayList<Grsklad>();
		try {
			in = OpenHttpConnection(urlString + "getgroups.php");
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(in);
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			} catch (SAXException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			doc.getDocumentElement().normalize();
			NodeList groupsNodes = doc.getElementsByTagName("group");

			for (int i = 0; i < groupsNodes.getLength(); i++) {
				Node groupNode = groupsNodes.item(i);

				if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
					Element groupElement = (Element) groupNode;
					// id
					NodeList idNodes = (groupElement)
							.getElementsByTagName("_id");
					Element idElement = (Element) idNodes.item(0);
					NodeList textNodes = idElement.getChildNodes();
					Integer id = Integer.parseInt(textNodes.item(0)
							.getNodeValue().toString());

					// name
					NodeList nameNodes = (groupElement)
							.getElementsByTagName("name");
					Element nameElement = (Element) nameNodes.item(0);
					textNodes = nameElement.getChildNodes();
					String name = "";
					if (textNodes.getLength() > 0)
						name = textNodes.item(0).getNodeValue()
								.toString();

					arr.add(new Grsklad(id, name));
				}

			}

		} catch (IOException e) {
			throw new IOException("Ошибка соединения.");
		}
		return arr;
	}

	public Integer UploadOrder(Integer orderId, Context c) throws IOException {
		OrdersDao odao = new OrdersDao(c);
		Order order = odao.getOrder(orderId);
		ArrayList<TOrder> tovari = order.getTovarlist();
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "order");
			serializer.startTag("", "datacrt");
			serializer.text(String.valueOf(order.getDocdate()));
			serializer.endTag("", "datacrt");
			serializer.startTag("", "manager_id");
			serializer.text(String.valueOf(this.m_id));
			serializer.endTag("", "manager_id");
			serializer.startTag("", "client_id");
			serializer.text(String.valueOf(order.getClient_id()));
			serializer.endTag("", "client_id");
			serializer.startTag("", "prim");
			serializer.text(String.valueOf(order.getPrim()));
			serializer.endTag("", "prim");
			serializer.startTag("", "prop");
			serializer.text(String.valueOf(order.getProperty()));
			serializer.endTag("", "prop");

			serializer.startTag("", "typepay");
			serializer.text(String.valueOf(order.getTypedoc()));
			serializer.endTag("", "typepay");
			serializer.startTag("", "typedoc");
			serializer.text(String.valueOf(order.getTypedoc()));
			serializer.endTag("", "typedoc");

			serializer.startTag("", "tovari");

			// serializer.attribute("", "number",
			// String.valueOf(tovari.size()));
			for (TOrder tovar : tovari) {
				serializer.startTag("", "tovar");
				serializer.startTag("", "_id");
				serializer.text(String.valueOf(tovar.getTovid()));
				serializer.endTag("", "_id");
				serializer.startTag("", "kolvo");
				serializer.text(String.valueOf(tovar.getTovkolvo()));
				serializer.endTag("", "kolvo");
				serializer.startTag("", "cena");
				serializer.text(String.valueOf(tovar.getTovcenands()));
				serializer.endTag("", "cena");
				serializer.endTag("", "tovar");
			}
			serializer.endTag("", "tovari");
			serializer.endTag("", "order");
			serializer.endDocument();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		byte[] xmlbytes = writer.toString().getBytes();
		URL url = new URL(urlString + "neworder.php");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6 * 1000);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", String
				.valueOf(xmlbytes.length));
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	//	conn.connect();
		try {
			OutputStream outStream = conn.getOutputStream();	
		
		
		outStream.write(xmlbytes);
		outStream.flush();
		outStream.close();
		} catch (Exception e) {
			String
			 s= e.getClass().toString();
			s= e.getMessage();
			s= e.getClass().toString();
		}
		InputStream in = null;
		int response = -1;
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Не HTTP соединение");
		try {
			HttpURLConnection httpConn = conn;
			// httpConn.setRequestMethod("POST");
			// httpConn.setAllowUserInteraction(false);
			// httpConn.setInstanceFollowRedirects(true);

			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();

				Document doc = null;
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db;
				try {
					db = dbf.newDocumentBuilder();
					// //////
			/*		
					  InputStreamReader isr = new InputStreamReader(in); int
					  charRead; String str=""; char[] inputBuffer=new
					  char[2000];
					  
					  try{ while ((charRead=isr.read(inputBuffer))>0){ String
					  readString=String.copyValueOf(inputBuffer,0,charRead);
					  str+=readString; inputBuffer=new char[2000]; }
					  in.close(); }catch (IOException ex2){
					  ex2.printStackTrace();
					  
					  }
				*/	 // /////
					doc = db.parse(in);
					// /////
				} catch (ParserConfigurationException ex) {
					ex.printStackTrace();
				} catch (SAXException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				doc.getDocumentElement().normalize();
				NodeList resNodes = doc.getElementsByTagName("real_order_id");
				Element resElement = (Element) resNodes.item(0);
				NodeList textNodes = resElement.getChildNodes();

				String res = "";
				if (textNodes.getLength() > 0)
					res = textNodes.item(0).getNodeValue().toString();
				return Integer.valueOf(res);
			}
		} catch (Exception ex) {
			throw new IOException("Ошибка соединения.");
		}
		return 0;
	}
	public Integer UploadGPS(  Double lat, Double lng) throws IOException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "gps");
			serializer.startTag("", "lat");
			serializer.text(String.valueOf(lat));
			serializer.endTag("", "lat");

			serializer.startTag("", "manager_id");
			serializer.text(String.valueOf(this.m_id));
			serializer.endTag("", "manager_id");
			
			serializer.startTag("", "lng");
			serializer.text(String.valueOf(lng));
			serializer.endTag("", "lng");
			serializer.endTag("", "gps");
			serializer.endDocument();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		byte[] xmlbytes = writer.toString().getBytes();
		URL url = new URL("http://85.90.210.161/trading/" + "gps.php");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6 * 1000);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", String
				.valueOf(xmlbytes.length));
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		try {
			OutputStream outStream = conn.getOutputStream();	
		
		
		outStream.write(xmlbytes);
		outStream.flush();
		outStream.close();
		} catch (Exception e) {
			String s=e.getMessage();
		}
		InputStream in = null;
		int response = -1;
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Не HTTP соединение");
		try {
			HttpURLConnection httpConn = conn;
			// httpConn.setRequestMethod("POST");
			// httpConn.setAllowUserInteraction(false);
			// httpConn.setInstanceFollowRedirects(true);

			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();

				Document doc = null;
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db;
				try {
					db = dbf.newDocumentBuilder();
					// //////
	/*				
					  InputStreamReader isr = new InputStreamReader(in); int
					  charRead; String str=""; char[] inputBuffer=new
					  char[2000];
					  
					  try{ while ((charRead=isr.read(inputBuffer))>0){ String
					  readString=String.copyValueOf(inputBuffer,0,charRead);
					  str+=readString; inputBuffer=new char[2000]; }
					  in.close(); }catch (IOException ex2){
					  ex2.printStackTrace();
					  
					  }
		*/			 // /////
					doc = db.parse(in);
					// /////
				} catch (ParserConfigurationException ex) {
					ex.printStackTrace();
				} catch (SAXException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				doc.getDocumentElement().normalize();
				NodeList resNodes = doc.getElementsByTagName("real_order_id");
				Element resElement = (Element) resNodes.item(0);
				NodeList textNodes = resElement.getChildNodes();

				String res = "";
				if (textNodes.getLength() > 0)
					res = textNodes.item(0).getNodeValue().toString();
				return Integer.valueOf(res);
			}
		} catch (Exception ex) {
			throw new IOException("Ошибка соединения.");
		}
		return 0;
	}
	  public ArrayList<Order> GetLastOrders() throws IOException{
		    InputStream in = null;
		    ArrayList<Order> arr = new ArrayList<Order>();
		    try {
		      in = OpenHttpConnection(urlString + "getlastorders.php");
		        
		      // /////
/*
		      
		        InputStreamReader isr = new InputStreamReader(in); int charRead;
		        String str=""; char[] inputBuffer=new char[2000];
		        
		        try{ while ((charRead=isr.read(inputBuffer))>0){ String
		        readString=String.copyValueOf(inputBuffer,0,charRead);
		        str+=readString; inputBuffer=new char[2000]; } in.close(); }catch
		        (IOException ex2){ ex2.printStackTrace();
		        
		        }
		       */
		      // /////
		      
		      Document doc = null;
		      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		      DocumentBuilder db;
		      try {
		        db = dbf.newDocumentBuilder();
		        doc = db.parse(in);
		      } catch (ParserConfigurationException ex) {
		        ex.printStackTrace();
		      } catch (SAXException e) {
		        // TODO: handle exception
		        e.printStackTrace();
		      }

		      doc.getDocumentElement().normalize();
		      NodeList parNodes = doc.getElementsByTagName("order");

		      for (int i = 0; i < parNodes.getLength(); i++) {
		        Node parNode = parNodes.item(i);

		        if (parNode.getNodeType() == Node.ELEMENT_NODE) {
		          Element parElement = (Element) parNode;
		          
		          // order_id
		          NodeList idNodes = (parElement).getElementsByTagName("order_id");
		          Element idElement = (Element) idNodes.item(0);
		          // ---get all the child nodes under the <title> element---
		          NodeList textNodes = idElement.getChildNodes();
		          Integer order_id = Integer.parseInt(textNodes.item(0)
		              .getNodeValue().toString());

		          // datacrt
		          NodeList dataNodes = (parElement)
		              .getElementsByTagName("datacrt");
		          Element dataElement = (Element) dataNodes.item(0);          
		          textNodes = dataElement.getChildNodes();
		          String datacrt = "";
		          if (textNodes.getLength() > 0)
		            datacrt = textNodes.item(0).getNodeValue()
		                .toString();

		          // client_id
		          NodeList client_idNodes = (parElement)
		              .getElementsByTagName("client_id");
		          Element client_idElement = (Element) client_idNodes.item(0);
		          textNodes = client_idElement.getChildNodes();
		          Integer client_id = 0;
		          if (textNodes.getLength() > 0)
 		            client_id = Integer.parseInt(textNodes.item(0).getNodeValue());
		          else
		        	  continue;

		          // manager_id
		          NodeList manager_idNodes = (parElement)
		              .getElementsByTagName("manager_id");
		          Element manager_idElement = (Element) manager_idNodes.item(0);
		          textNodes = manager_idElement.getChildNodes();
		          Integer manager_id = Integer.parseInt(textNodes.item(0).getNodeValue());
		          
		          // prim
		          NodeList primNodes = (parElement)
		              .getElementsByTagName("prim");
		          Element primElement = (Element) primNodes.item(0);          
		          textNodes = primElement.getChildNodes();
		          String prim = "";
		          if (textNodes.getLength() > 0)
		            prim = textNodes.item(0).getNodeValue()
		                .toString();
		          
		          // typedoc
		          NodeList typedocNodes = (parElement)
		              .getElementsByTagName("typedoc");
		          Element typedocElement = (Element) typedocNodes.item(0);
		          textNodes = typedocElement.getChildNodes();
		          Integer typedoc = Integer.parseInt(textNodes.item(0).getNodeValue());

		          // arr.add(new Partner(id, name, address, phone, 0, 0.0,2));
		          Order ord=new Order();
		          
		          ord.setClient_id(client_id);
		          ord.setDocdate(Date.valueOf(datacrt.substring(0, 10)));
		          ord.setDoctime(Time.valueOf(datacrt.substring(11, datacrt.length())));
		          ord.setCentral_id(order_id);
		          ord.setTypedoc(typedoc);
		          ord.setTypepay(2);
		          ord.setPrim(prim);
		          ord.setProperty(prop);
		          ord.setDocstate(Const.DOC_STATE_NEW);
		          
		          NodeList tovariNode = parElement.getElementsByTagName("tovari");
		          if (tovariNode.getLength()>0){
		        	  Element tovariElement = (Element) tovariNode.item(0);
		        	  NodeList tovariNodes = tovariElement.getChildNodes();
			          for (int j = 0; j < tovariNodes.getLength(); j++) {
			            Node tovarNode = tovariNodes.item(j);
	
			            if (tovarNode.getNodeType() == Node.ELEMENT_NODE) {
			              Element tovarElement = (Element) tovarNode;
			              
			              // _id
			              NodeList _idNodes = tovarElement.getElementsByTagName("_id");
			              Element _idElement = (Element) _idNodes.item(0);
			              textNodes = _idElement.getChildNodes();
			              Integer _id = Integer.parseInt(textNodes.item(0)
			                  .getNodeValue().toString());
			              
			              // kolvo
			              NodeList kolvoNodes = tovarElement.getElementsByTagName("kolvo");
			              Element kolvoElement = (Element) kolvoNodes.item(0);
			              textNodes = kolvoElement.getChildNodes();
			              Double kolvo = Double.parseDouble(textNodes.item(0)
			                  .getNodeValue().toString());
			              
			              // cena
			              NodeList cenaNodes = tovarElement.getElementsByTagName("cena");
			              Element cenaElement = (Element) cenaNodes.item(0);
			              textNodes = cenaElement.getChildNodes();
			              Double cena = Double.parseDouble(textNodes.item(0)
			                  .getNodeValue().toString());
			              
			              TOrder tord=new TOrder();
			              tord.setTovid(_id);
			              tord.setTovcenands(cena);
			              tord.setTovkolvo(kolvo);
			              
			              ord.addTovar(tord);
			              
			            }
			          }
		          }
		          arr.add(ord);
		        }

		      }

		    } catch (IOException e) {
		      throw new IOException("Ошибка соединения.");
		    }
		    return arr;
		  }
	  
		public Integer UploadCard(Integer orderId, Context c) throws IOException {
			int ret=-1;
			ClientCardsDao ccd = new ClientCardsDao(c);
			CC_card cc_cd = ccd.getCC_card(orderId, false,0, true);
			ArrayList<CC_data> tovari = cc_cd.getCc_data();
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			try {
				serializer.setOutput(writer);
				serializer.startDocument("UTF-8", true);
				serializer.startTag("", "cc");
				serializer.startTag("", "dt");
				serializer.text(String.valueOf(cc_cd.getDt()));
				serializer.endTag("", "dt");
				serializer.startTag("", "manager_id");
				serializer.text(String.valueOf(this.m_id));
				serializer.endTag("", "manager_id");
				serializer.startTag("", "client_id");
				serializer.text(String.valueOf(cc_cd.getClient_id()));
				serializer.endTag("", "client_id");
				serializer.startTag("", "card_id");
				serializer.text(String.valueOf(cc_cd.getClientcard_id()));
				serializer.endTag("", "card_id");
				serializer.startTag("", "prim");
				serializer.text(String.valueOf(cc_cd.getPrim()));
				serializer.endTag("", "prim");
				serializer.startTag("", "tovari");

				// serializer.attribute("", "number",
				// String.valueOf(tovari.size()));
				for (CC_data tovar : tovari) {
					serializer.startTag("", "tovar");
					serializer.startTag("", "position_id");
					serializer.text(String.valueOf(tovar.getPosition_id()));
					serializer.endTag("", "position_id");
					serializer.startTag("", "card_id");
					serializer.text(String.valueOf(tovar.getCard_id()));
					serializer.endTag("", "card_id");
					serializer.startTag("", "ost");
					serializer.text(String.valueOf(tovar.getOst()));
					serializer.endTag("", "ost");
					serializer.startTag("", "zakaz");
					serializer.text(String.valueOf(tovar.getZakaz()));
					serializer.endTag("", "zakaz");
					serializer.endTag("", "tovar");
				}
				serializer.endTag("", "tovari");
				serializer.endTag("", "cc");
				serializer.endDocument();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			byte[] xmlbytes = writer.toString().getBytes();
			URL url = new URL(urlString + "newcard.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6 * 1000);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Length", String
					.valueOf(xmlbytes.length));
			conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			try {
				OutputStream outStream = conn.getOutputStream();	
			
			
			outStream.write(xmlbytes);
			
			
			
			outStream.flush();
			outStream.close();
			} catch (Exception e) {
				String s=e.getMessage();
			}
			InputStream in = null;
			int response = -1;
			if (!(conn instanceof HttpURLConnection))
				throw new IOException("Не HTTP соединение");
			try {
				HttpURLConnection httpConn = conn;
				// httpConn.setRequestMethod("POST");
				// httpConn.setAllowUserInteraction(false);
				// httpConn.setInstanceFollowRedirects(true);

				httpConn.connect();

				response = httpConn.getResponseCode();
				if (response == HttpURLConnection.HTTP_OK) {
					in = httpConn.getInputStream();

					Document doc = null;
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db;
					try {
						db = dbf.newDocumentBuilder();
						// //////
						
/*						  InputStreamReader isr = new InputStreamReader(in); int
						  charRead; String str=""; char[] inputBuffer=new
						  char[2000];
						  
						  try{ while ((charRead=isr.read(inputBuffer))>0){ String
						  readString=String.copyValueOf(inputBuffer,0,charRead);
						  str+=readString; inputBuffer=new char[2000]; }
						  in.close(); }catch (IOException ex2){
						  ex2.printStackTrace();
						  
						  }
						  
						 // /////*/
						doc = db.parse(in);
						// /////
					} catch (ParserConfigurationException ex) {
						ex.printStackTrace();
					} catch (SAXException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					doc.getDocumentElement().normalize();
					NodeList resNodes = doc.getElementsByTagName("real_order_id");
					Element resElement = (Element) resNodes.item(0);
					NodeList textNodes = resElement.getChildNodes();

					String res = "";
					if (textNodes.getLength() > 0)
						res = textNodes.item(0).getNodeValue().toString();
					ret=Integer.valueOf(res);
					return ret;
				}
			} catch (Exception ex) {
				throw new IOException("Ошибка соединения.");
			}
			return ret;
		}
	  
	public ArrayList<ClientCard> GetCardTemplates() throws IOException{
		    InputStream in = null;
		    ArrayList<ClientCard> arr = new ArrayList<ClientCard>();
		    try {
		      in = OpenHttpConnection(urlString + "getcardtemplates.php");
		        
		      // /////
/*
		      
		        InputStreamReader isr = new InputStreamReader(in); int charRead;
		        String str=""; char[] inputBuffer=new char[2000];
		        
		        try{ while ((charRead=isr.read(inputBuffer))>0){ String
		        readString=String.copyValueOf(inputBuffer,0,charRead);
		        str+=readString; inputBuffer=new char[2000]; } in.close(); }catch
		        (IOException ex2){ ex2.printStackTrace();
		        
		        }
		       */
		      // /////
		      
		      Document doc = null;
		      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		      DocumentBuilder db;
		      try {
		        db = dbf.newDocumentBuilder();
		        doc = db.parse(in);
		      } catch (ParserConfigurationException ex) {
		        ex.printStackTrace();
		      } catch (SAXException e) {
		        // TODO: handle exception
		        e.printStackTrace();
		      }

		      doc.getDocumentElement().normalize();
		      NodeList parNodes = doc.getElementsByTagName("card_templ");

		      for (int i = 0; i < parNodes.getLength(); i++) {
		        Node parNode = parNodes.item(i);

		        if (parNode.getNodeType() == Node.ELEMENT_NODE) {
		          Element parElement = (Element) parNode;
		          
		          // templ_id
		          NodeList idNodes = (parElement).getElementsByTagName("templ_id");
		          Element idElement = (Element) idNodes.item(0);
		          // ---get all the child nodes under the <title> element---
		          NodeList textNodes = idElement.getChildNodes();
		          Integer order_id = Integer.parseInt(textNodes.item(0)
		              .getNodeValue().toString());

		          // name
		          NodeList nameNodes = (parElement)
		              .getElementsByTagName("name");
		          Element nameElement = (Element) nameNodes.item(0);          
		          textNodes = nameElement.getChildNodes();
		          String name = "";
		          if (textNodes.getLength() > 0)
		            name = textNodes.item(0).getNodeValue()
		                .toString();

		          
		          ClientCard cc=new ClientCard(order_id, name);		          
				  //!!! создать объект шаблона карты
		          
		          
		          NodeList tovariNode = parElement.getElementsByTagName("tovari");
		          if (tovariNode.getLength()>0){
		        	  Element tovariElement = (Element) tovariNode.item(0);
		        	  NodeList tovariNodes = tovariElement.getChildNodes();
			          for (int j = 0; j < tovariNodes.getLength(); j++) {
			            Node tovarNode = tovariNodes.item(j);
	
			            if (tovarNode.getNodeType() == Node.ELEMENT_NODE) {
			              Element tovarElement = (Element) tovarNode;

			              // ID
			              NodeList _idNodes = tovarElement.getElementsByTagName("_id");
			              Element _idElement = (Element) _idNodes.item(0);
			              textNodes = _idElement.getChildNodes();
			              Integer _id = Integer.parseInt(textNodes.item(0)
			                  .getNodeValue().toString());

			              // TOVAR_ID
			              NodeList tovar_idNodes = tovarElement.getElementsByTagName("tovar_id");
			              Element tovar_idElement = (Element) tovar_idNodes.item(0);
			              textNodes = tovar_idElement.getChildNodes();
			              Integer tovar_id = Integer.parseInt(textNodes.item(0)
			                  .getNodeValue().toString());
			              
			              // cat_a
			              NodeList cat_aNodes = tovarElement.getElementsByTagName("cat_a");
			              Element cat_aElement = (Element) cat_aNodes.item(0);
			              textNodes = cat_aElement.getChildNodes();
			              Double cat_a ;
			              if (textNodes.getLength() > 0)
			              cat_a = Double.parseDouble(textNodes.item(0)
			                  .getNodeValue().toString());
			              else
			            	  cat_a=0.0;
			            	  
			              
			              // cat_b
			              NodeList cat_bNodes = tovarElement.getElementsByTagName("cat_b");
			              Element cat_bElement = (Element) cat_bNodes.item(0);
			              textNodes = cat_bElement.getChildNodes();
			              Double cat_b ;
			              if (textNodes.getLength() > 0)
				              cat_b = Double.parseDouble(textNodes.item(0)
				                  .getNodeValue().toString());
				              else
				            	  cat_b=0.0;
				            	
						  
						  // cat_c
			              NodeList cat_cNodes = tovarElement.getElementsByTagName("cat_c");
			              Element cat_cElement = (Element) cat_cNodes.item(0);
			              textNodes = cat_cElement.getChildNodes();
			              Double cat_c ;
			              if (textNodes.getLength() > 0)
				              cat_c = Double.parseDouble(textNodes.item(0)
				                  .getNodeValue().toString());
				              else
				            	  cat_c=0.0;
				          
			              cc_tovari tov=new cc_tovari(_id,order_id, tovar_id,cat_a,cat_b,cat_c  );
			              cc.AddTovar(tov);
						  //!!!!! Забить товары в карту
			              
			            }
			          }
		          }
		          arr.add(cc);
		        }

		      }

		    } catch (IOException e) {
		      throw new IOException("Ошибка соединения.");
		    }
		    return arr;
		  }  

	public ArrayList<CC_card> GetCC_Card() throws IOException{
	    InputStream in = null;
	    ArrayList<CC_card> arr = new ArrayList<CC_card>();
	    try {
	      in = OpenHttpConnection(urlString + "getcards.php");
	        
	      // /////
/*
	      
	        InputStreamReader isr = new InputStreamReader(in); int charRead;
	        String str=""; char[] inputBuffer=new char[2000];
	        
	        try{ while ((charRead=isr.read(inputBuffer))>0){ String
	        readString=String.copyValueOf(inputBuffer,0,charRead);
	        str+=readString; inputBuffer=new char[2000]; } in.close(); }catch
	        (IOException ex2){ ex2.printStackTrace();
	        
	        }
////   */
	      // /////
	      
	      Document doc = null;
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db;
	      try {
	        db = dbf.newDocumentBuilder();
	        doc = db.parse(in);
	      } catch (ParserConfigurationException ex) {
	        ex.printStackTrace();
	      } catch (SAXException e) {
	        // TODO: handle exception
	        e.printStackTrace();
	      }

	      doc.getDocumentElement().normalize();
	      NodeList parNodes = doc.getElementsByTagName("c");

	      for (int i = 0; i < parNodes.getLength(); i++) {
	        Node parNode = parNodes.item(i);

	        if (parNode.getNodeType() == Node.ELEMENT_NODE) {
	          Element parElement = (Element) parNode;
	          
	          // order_id
	          NodeList idNodes = (parElement).getElementsByTagName("_id");
	          Element idElement = (Element) idNodes.item(0);
	          // ---get all the child nodes under the <title> element---
	          NodeList textNodes = idElement.getChildNodes();
	          Integer order_id = Integer.parseInt(textNodes.item(0)
	              .getNodeValue().toString());

	          // datacrt
	          NodeList dataNodes = (parElement)
	              .getElementsByTagName("dt");
	          Element dataElement = (Element) dataNodes.item(0);          
	          textNodes = dataElement.getChildNodes();
	          String datacrt = "";
	          if (textNodes.getLength() > 0)
	            datacrt = textNodes.item(0).getNodeValue()
	                .toString();

	          // client_id
	          NodeList client_idNodes = (parElement)
	              .getElementsByTagName("ci");
	          Element client_idElement = (Element) client_idNodes.item(0);
	          textNodes = client_idElement.getChildNodes();
	          Integer client_id = 0;
	          if (textNodes.getLength() > 0)
		            client_id = Integer.parseInt(textNodes.item(0).getNodeValue());
	          else
	        	  continue;

	          // manager_id
	          NodeList manager_idNodes = (parElement)
	              .getElementsByTagName("cai");
	          Element manager_idElement = (Element) manager_idNodes.item(0);
	          textNodes = manager_idElement.getChildNodes();
	          Integer card_id = Integer.parseInt(textNodes.item(0).getNodeValue());
	          
	          // prim
	          NodeList primNodes = (parElement)
	              .getElementsByTagName("p");
	          Element primElement = (Element) primNodes.item(0);          
	          textNodes = primElement.getChildNodes();
	          String prim = "";
	          if (textNodes.getLength() > 0)
	            prim = textNodes.item(0).getNodeValue()
	                .toString();
	          

	          // arr.add(new Partner(id, name, address, phone, 0, 0.0,2));
	          CC_card ord=new CC_card();
	          
	          ord.setClient_id(client_id);
	          ord.setDt(Date.valueOf(datacrt.substring(0, 10)));
	          ord.setClientcard_id(card_id);
	          ord.setPrim(prim);
	          ord.setCentral_id(order_id);
	          
	          NodeList tovariNode = parElement.getElementsByTagName("cd");
	          if (tovariNode.getLength()>0){
	        	  Element tovariElement = (Element) tovariNode.item(0);
	        	  NodeList tovariNodes = tovariElement.getChildNodes();
		          for (int j = 0; j < tovariNodes.getLength(); j++) {
		            Node tovarNode = tovariNodes.item(j);

		            if (tovarNode.getNodeType() == Node.ELEMENT_NODE) {
		              Element tovarElement = (Element) tovarNode;
		              
		              // _id
		              NodeList _idNodes = tovarElement.getElementsByTagName("pi");
		              Element _idElement = (Element) _idNodes.item(0);
		              textNodes = _idElement.getChildNodes();
		              Integer position_id = Integer.parseInt(textNodes.item(0)
		                  .getNodeValue().toString());
		              
		              // kolvo
		              NodeList kolvoNodes = tovarElement.getElementsByTagName("o");
		              Element kolvoElement = (Element) kolvoNodes.item(0);
		              textNodes = kolvoElement.getChildNodes();
		              Double ost = Double.parseDouble(textNodes.item(0)
		                  .getNodeValue().toString());
		              
		              // cena
		              NodeList cenaNodes = tovarElement.getElementsByTagName("z");
		              Element cenaElement = (Element) cenaNodes.item(0);
		              textNodes = cenaElement.getChildNodes();
		              Double zakaz = Double.parseDouble(textNodes.item(0)
		                  .getNodeValue().toString());
		              
		              CC_data tord=new CC_data();
		              tord.setPosition_id(position_id);
		              tord.setOst(ost);
		              tord.setZakaz(zakaz);
		              
		              ord.Add_cc_data(tord);
		              
		            }
		          }
	          }
	          arr.add(ord);
	        }

	      }

	    } catch (IOException e) {
	      throw new IOException("Ошибка соединения.");
	    }
	    return arr;
	  }
	

	public String GetNewProgramVersion2(){
		String verName="";
		try {
			final URL url = new URL(urlString + "version.php");
			InputStream in=OpenHttpConnection(urlString + "version.php");
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br=new BufferedReader(isr);
			String line,line1 = "";
			try
			{
				while ((line = br.readLine()) != null)
					verName=line;
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public String GetNewProgremVersion(){
		InputStream in = null;
		final String[] newVer = {""};

		try {
			final URL url = new URL(urlString + "version.php");
		AsyncTask at=	new AsyncTask(){


			protected void onPostExecute(String o) {

				newVer[0]=o;
			}


				protected String doInBackground(Object[] objects) {
				String verName="";
					try {

						InputStream in = null;
						int response = -1;
						//	System.setProperty("http.keepAlive", "false");
						URL url = new URL(objects[0].toString());
						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);
						String data = "manager_id=" + URLEncoder.encode(m_id.toString())
                                + "&prop=" + URLEncoder.encode(prop.toString());
						conn.setRequestProperty("Pragma", "no-cache");
						conn.setRequestProperty("Content-type",
                                "application/x-www-form-urlencoded");
//		conn.setRequestProperty("Content-length", Integer.toString(data.length()));

						if (!(conn instanceof HttpURLConnection))
							throw new IOException("Не HTTP соединение");
						try {

							HttpURLConnection httpConn = (HttpURLConnection) conn;
							// httpConn.setRequestMethod("POST");
							// httpConn.setAllowUserInteraction(false);
							// httpConn.setInstanceFollowRedirects(true);


							httpConn.connect();

							response = httpConn.getResponseCode();

							if (response == HttpURLConnection.HTTP_OK) {
								in = httpConn.getInputStream();
								InputStreamReader isr = new InputStreamReader(in);
								BufferedReader br=new BufferedReader(isr);
								String line,line1 = "";
								try
								{
									while ((line = br.readLine()) != null)
										verName=line;
								}catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						} catch (Exception ex) {
							throw new IOException("Ошибка соединения.");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return verName;

				}
			}.execute(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return newVer[0];
	}

	public void GetProgram() throws IOException{
		InputStream in = null;
		final String[] newVer = {""};

		try {
			final URL url = new URL(urlString + "SPTrading.apk");
			new AsyncTask(){


				@Override
				protected Object doInBackground(Object[] objects) {
					try {
						InputStream in = null;
						int response = -1;
						//	System.setProperty("http.keepAlive", "false");
						URL url = new URL(objects[0].toString());
						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);
						String data = "manager_id=" + URLEncoder.encode(m_id.toString())
								+ "&prop=" + URLEncoder.encode(prop.toString());
						conn.setRequestProperty("Pragma", "no-cache");
						conn.setRequestProperty("Content-type",
								"application/x-www-form-urlencoded");
//		conn.setRequestProperty("Content-length", Integer.toString(data.length()));

						if (!(conn instanceof HttpURLConnection))
							throw new IOException("Не HTTP соединение");
						try {

							HttpURLConnection httpConn = (HttpURLConnection) conn;
							// httpConn.setRequestMethod("POST");
							// httpConn.setAllowUserInteraction(false);
							// httpConn.setInstanceFollowRedirects(true);


							httpConn.connect();

							response = httpConn.getResponseCode();

							if (response == HttpURLConnection.HTTP_OK) {
								in = httpConn.getInputStream();



								DataInputStream dis = new DataInputStream(in);

								byte[] buffer = new byte[1024];
								int length;

								FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "Download/SPTrading.apk"));
								while ((length = dis.read(buffer))>0) {
									fos.write(buffer, 0, length);
								}
								fos.close();


								//File f=new File(Environment.getExternalStorageDirectory() + "/" + "Download/","SPTrading.apk").
							}
						} catch (Exception ex) {
							throw new IOException("Ошибка соединения.");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return newVer[0];
				}
			}.execute(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}
}
