package services;

import EntityBean.Employee;

public class GeneralService {

	public Employee setObject(int Id, String firstName, String lastName) {
		
		Employee emp = new Employee();
		emp.setId(Id);
		emp.setFirstName(firstName);
		emp.setLastName(lastName);
		
		return emp;
		
	}

}
