/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two.
 * The identity of an element of the list is defined by its email.
 *
 * @author aurel.medvegy
 */
public class WorkersAndEmployeesMerge extends AbstractMessageTransformer {

    public static final String WORKDAY_WORKERS = "workersFromWorkday";
    public static final String SAP_EMPLOYEES = "employeesFromSap";

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

        return mergeList(getWorkersList(message, WORKDAY_WORKERS), getUsersList(message, SAP_EMPLOYEES));
    }

    private List<Map<String, String>> getUsersList(MuleMessage message, String propertyName) {
        return message.getInvocationProperty(propertyName);
    }

    private List<Map<String, String>> getWorkersList(MuleMessage message, String propertyName) {
        return message.getInvocationProperty(propertyName);
    }

    /**
     * The method will merge the users from the two lists creating a new one.
     *
     * @param workersFromWorkday  users from Workday
     * @param employeesFromSap users from SAP
     * @return a list with the merged content of the to input lists
     */
    private List<Map<String, String>> mergeList(List<Map<String, String>> workersFromWorkday, List<Map<String, String>> employeesFromSap) {

        List<Map<String, String>> mergedUsersList = new ArrayList<Map<String, String>>();

        // Put all users from Workday in the merged mergedUsersList
        for (Map<String, String> userFromWorkday : workersFromWorkday) {
            Map<String, String> mergedUser = createMergedUser(userFromWorkday);
            mergedUser.put("IDInWorkday", userFromWorkday.get("Id"));
            mergedUser.put("WorkerNameInWorkday", userFromWorkday.get("Username"));
            mergedUsersList.add(mergedUser);
        }

        // Add the new users from Salesforce and update the exiting ones
        for (Map<String, String> userFromSalesforce : employeesFromSap) {
            Map<String, String> userFromWorkday = findUserInList(userFromSalesforce.get("Email"), mergedUsersList);
            if (userFromWorkday != null) {
                userFromWorkday.put("IDInSalesforce", userFromSalesforce.get("Id"));
                userFromWorkday.put("UserNameInSalesforce", userFromSalesforce.get("Username"));
            } else {
                Map<String, String> mergedUser = createMergedUser(userFromSalesforce);
                mergedUser.put("IDInSalesforce", userFromSalesforce.get("Id"));
                mergedUser.put("UserNameInSalesforce", userFromSalesforce.get("Username"));
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
        mergedUser.put("IDInSalesforce", "");
        mergedUser.put("UserNameInSalesforce", "");
        return mergedUser;
    }

    private Map<String, String> findUserInList(String email, List<Map<String, String>> userList) {

        for (Map<String, String> user : userList) {
            if (user.get("Email").equals(email)) {
                return user;
            }
        }
        return null;
    }
}
