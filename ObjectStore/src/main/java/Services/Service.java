package Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import com.google.gson.Gson;

import EntityBean.Employee;

public class Service {

	Gson gson = new Gson();
	public static String upload_directory = "/home/local/ZOHOCORP/jagadeesh-11774/Documents/ObjectStore";

	public boolean StoreFile(String data) {

		boolean flag = true;

		try {
			Employee emp = gson.fromJson(data, Employee.class);

			System.out.println(" here " + emp.getId());

			Properties p = new Properties();
			p.setProperty("Id", String.valueOf(emp.getId()));
			p.setProperty("firstName", emp.getFirstName());
			p.setProperty("lastName", emp.getLastName());

			FileWriter fw = new FileWriter(upload_directory + File.separator + emp.getId() + ".txt");
			p.store(fw, "Stored person");
			fw.close();
		} catch (Exception e) {
			flag = false;
			System.out.println(" Exception occured");
		}
		return flag;
	}

	public boolean checkFile(String fileName) {

		boolean flag = false;

		File f = new File(upload_directory);
		String[] fileList = f.list();
		for (String str : fileList) {
			if (fileName.equals(str)) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	public JSONObject getData(String fileName) throws IOException {

		JSONObject res = new JSONObject();
		try {
			Properties p = new Properties();
			FileReader fr = new FileReader(upload_directory + File.separator + fileName);
			p.load(fr);
			fr.close();

			res.put("Id", p.getProperty("Id"));
			res.put("firstName", p.getProperty("firstName"));
			res.put("lastName", p.getProperty("lastName"));
		} catch (FileNotFoundException e) {
			System.out.println(" File Not Found exception ");
		}

		return res;
	}

}
