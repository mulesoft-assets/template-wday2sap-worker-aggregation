/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two.
 * The identity of an element of the list is defined by its email.
 *
 * @author aurel.medvegy
 */
public class WorkersAndEmployeesMerge {

    /**
     * The method will merge the users from the two lists creating a new one.
     *
     * @param workersFromWorkday  users from Workday
     * @param employeesFromSap users from SAP
     * @return a list with the merged content of the to input lists
     */
    List<Map<String, String>> mergeList(List<Map<String, String>> workersFromWorkday, List<Map<String, String>> employeesFromSap) {

        List<Map<String, String>> mergedUsersList = new ArrayList<Map<String, String>>();

        // Put all users from Workday in the merged mergedUsersList
        for (Map<String, String> userFromWorkday : workersFromWorkday) {
            Map<String, String> mergedUser = createMergedUser(userFromWorkday);
            mergedUser.put("IDInWorkday", userFromWorkday.get("Id"));
            mergedUser.put("WorkerNameInWorkday", userFromWorkday.get("Username"));
            mergedUsersList.add(mergedUser);
        }

		Map<String, String> employee;
		HashMap<String, Map<String, String>> ids = new HashMap<String, Map<String, String>>();

		for (Map<String, String> sapEmployee : employeesFromSap) {

			String id = sapEmployee.get("Id");
			if (id == null) {
				continue;
			}

			employee = ids.get(id);
			if (employee == null) {
				employee = new HashMap<String, String>();
				employee.put("Id", id);
			}

			String firstName = sapEmployee.get("FirstName");
			String lastName = sapEmployee.get("LastName");
			if (firstName != null || lastName != null) {
				StringBuilder builder = new StringBuilder();

				if (firstName != null) {
					builder.append(firstName);
				}
				if (builder.length() > 0 && lastName != null) {
					builder.append(" ");
				}
				if (lastName != null) {
					builder.append(lastName);
				}

				employee.put("Name", builder.toString());
			}

			String username = sapEmployee.get("Username");
			if (username != null) {
				employee.put("Username", username);
			}

			String email = sapEmployee.get("Email");
			if (email != null) {
				employee.put("Email", email);
			}

			ids.put(id, employee);
		}

		employeesFromSap = Lists.newArrayList(ids.values());

		Iterator<Map<String, String>> it = employeesFromSap.iterator();
		while (it.hasNext()) {
			Map<String, String> next = it.next();
			if (next.get("Email") == null) {
				it.remove();
			}
		}
        
		// Add the new users from SAP and update the exiting ones
        for (Map<String, String> userFromSAP : employeesFromSap) {
            Map<String, String> userFromWorkday = findUserInList(userFromSAP.get("Email"), mergedUsersList);
            if (userFromWorkday != null) {
                userFromWorkday.put("IDInSap", userFromSAP.get("Id"));
                userFromWorkday.put("UserNameInSap", userFromSAP.get("Username"));
            } else {
                Map<String, String> mergedUser = createMergedUser(userFromSAP);
                mergedUser.put("IDInSap", userFromSAP.get("Id"));
                mergedUser.put("UserNameInSap", userFromSAP.get("Username"));
                mergedUsersList.add(mergedUser);
            }

        }
        return mergedUsersList;
    }

    private Map<String, String> createMergedUser(Map<String, String> user) {

        Map<String, String> mergedUser = new HashMap<String, String>();
        mergedUser.put("Email", user.get("Email"));
        mergedUser.put("Name", user.get("Name"));
        mergedUser.put("IDInWorkday", "");
        mergedUser.put("WorkerNameInWorkday", "");
        mergedUser.put("IDInSap", "");
        mergedUser.put("UserNameInSap", "");
        return mergedUser;
    }

    private Map<String, String> findUserInList(String email, List<Map<String, String>> userList) {

        for (Map<String, String> user : userList) {
            if (user.get("Email").equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}
