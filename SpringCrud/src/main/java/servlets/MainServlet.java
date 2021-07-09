package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import EntityBean.Employee;
import services.GeneralService;

@RestController
@RequestMapping("/person")
public class MainServlet {

	public static String Endpoint = "http://localhost:8181/ObjectStore/check";
	GeneralService serv = new GeneralService();

	JSONObject resp = new JSONObject();

	@RequestMapping(name = "{Id}", method = RequestMethod.GET)
	public void getObjects(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "Id") int Id) throws IOException {

		String charset = "UTF-8";
		String query = String.format("Id=%s", URLEncoder.encode(String.valueOf(Id), charset));
		URL url = new URL(Endpoint + "?" + query);//
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Accept-Charset", charset);

		con.setRequestMethod("GET");
		con.connect();

		if (con.getResponseCode() == 200) {
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String line;
			StringBuffer res = new StringBuffer();
	 
			while ((line = in.readLine()) != null) {
				res.append(line);
			}
			in.close();
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.print(res);
			writer.flush();

		} else {
			resp.put("result", "File not Found");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();
		}
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public void postObjects(HttpServletRequest request, HttpServletResponse response, @RequestParam("Id") int Id, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws IOException {
		Employee emp = new Employee();
		emp = serv.setObject(Id, firstName, lastName);

		Gson gson = new Gson();
		String json = gson.toJson(emp);

		System.out.println(" Json form of object" + json);

		URL url = new URL(Endpoint);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");

		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(json.getBytes("utf-8"));
		os.flush();
		os.close();

		if (con.getResponseCode() == 200) {

			resp.put("result", " Added Successfully ");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();

		} else {
			resp.put("result", " Operation Unsuccessful ");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();
		}

	}

}
