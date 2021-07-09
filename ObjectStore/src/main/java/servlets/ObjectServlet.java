package servlets;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import EntityBean.Employee;
import Services.Service;

@RestController
@RequestMapping("/check")
public class ObjectServlet {

	Employee emp = new Employee();
	Gson gson = new Gson();
	public static String upload_directory = "/home/local/ZOHOCORP/jagadeesh-11774/Documents/ObjectStore";

	Service serv = new Service();

	@RequestMapping(name = "{Id}", method = RequestMethod.GET)
	public void getId(@RequestParam(name = "Id") String Id, HttpServletResponse response) throws IOException {

		String fileName = Id + ".txt";
		JSONObject resp = new JSONObject();

		boolean flag = serv.checkFile(fileName);

		if (!flag) {
			resp.put("result", " File Doesn't Exist");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();
		} else {

			JSONObject res = serv.getData(fileName);
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.print(res);
			writer.flush();

		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public void postObj(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String data = IOUtils.toString(request.getInputStream(), "UTF-8");
		JSONObject resp = new JSONObject();
		boolean res = serv.StoreFile(data);

		if (res) {
			resp.put("result", " Added Successfully ");
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();
		} else {
			resp.put("result", " Operation Unsuccessful ");
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.print(resp);
			writer.flush();
		}

	}
}
