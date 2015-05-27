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

import junit.framework.Assert;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.api.MuleContext;
import org.mule.api.transformer.TransformerException;

@RunWith(MockitoJUnitRunner.class)
public class WorkersAndEmployeesMergeTest {

	private final static Logger LOGGER = LogManager.getLogger(WorkersAndEmployeesMergeTest.class);
	
	@Mock
	private MuleContext muleContext;

	@Test
	public void testMerge() throws TransformerException {
		List<Map<String, String>> leadsA = createUserLists("A", 0, 1);
		List<Map<String, String>> leadsB = createUserLists("B", 1, 2);
		
		WorkersAndEmployeesMerge sfdcLeadMerge = new WorkersAndEmployeesMerge();
		List<Map<String, String>> mergedList = sfdcLeadMerge.mergeList(leadsA, leadsB);

		LOGGER.info(mergedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), mergedList);

	}

	static List<Map<String, String>> createExpectedList() {
		Map<String, String> user0 = new HashMap<String, String>();
		user0.put("IDInWorkday", "0");
		user0.put("IDInSap", "");
		user0.put("Email", "some.email.0@fakemail.com");
		user0.put("Name", "SomeName_0");
		user0.put("WorkerNameInWorkday", "username_0_A");
		user0.put("UserNameInSap", "");

		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("IDInWorkday", "1");
		user1.put("IDInSap", "1");
		user1.put("Email", "some.email.1@fakemail.com");
		user1.put("Name", "SomeName_1");
		user1.put("WorkerNameInWorkday", "username_1_A");
		user1.put("UserNameInSap", "username_1_B");

		Map<String, String> user2 = new HashMap<String, String>();
		user2.put("IDInWorkday", "");
		user2.put("IDInSap", "2");
		user2.put("Email", "some.email.2@fakemail.com");
		user2.put("Name", "SomeName_2");
		user2.put("WorkerNameInWorkday", "");
		user2.put("UserNameInSap", "username_2_B");

		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.add(user0);
		userList.add(user1);
		userList.add(user2);

		return userList;

	}

	static List<Map<String, String>> createUserLists(String orgId, int start, int end) {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		for (int i = start; i <= end; i++) {
			userList.add(createUser(orgId, i));
		}
		return userList;
	}

	static Map<String, String> createUser(String orgId, int sequence) {
		Map<String, String> user = new HashMap<String, String>();

		user.put("Id", new Integer(sequence).toString());
		user.put("Username", "username_" + sequence + "_" + orgId);
		user.put("Name", "SomeName_" + sequence);
		user.put("FirstName", "SomeName_" + sequence);
		user.put("Email", "some.email." + sequence + "@fakemail.com");

		return user;
	}
}
