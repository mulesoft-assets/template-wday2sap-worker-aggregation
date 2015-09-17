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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleConfiguration;
import org.mule.api.transformer.TransformerException;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class SortWorkersAndEmployeesListTest {
	
	private final static Logger LOGGER = LogManager.getLogger(SortWorkersAndEmployeesListTest.class);
	
	@Mock
	private MuleContext muleContext;
	
	@Mock
	private MuleConfiguration muleConfiguration;

	@Test
	public void testSort() throws TransformerException {
		
		Mockito.when(muleContext.getConfiguration()).thenReturn(muleConfiguration);
		Mockito.when(muleConfiguration.getDefaultEncoding()).thenReturn("UTF-8");

		MuleMessage message = new DefaultMuleMessage(WorkersAndEmployeesMergeTest.createExpectedList(), muleContext);

		SortWorkersAndEmployeesList transformer = new SortWorkersAndEmployeesList();
		List<Map<String, String>> sortedList = (List<Map<String, String>>) transformer.transform(message, "UTF-8");

		LOGGER.info(sortedList);
		Assert.assertEquals("The merged list obtained is not as expected", createOriginalList(), sortedList);

	}

	private List<Map<String, String>> createOriginalList() {
		Map<String, String> user0 = new HashMap<String, String>();
		user0.put("IDInWorkday", "0");
		user0.put("IDInSap", "");
		user0.put("Email", "some.email.0@fakemail.com");
		user0.put("Name", "SomeName_0");
		user0.put("WorkerNameInWorkday", "username_0_A");
		user0.put("UserNameInSap", "");		

		Map<String, String> user2 = new HashMap<String, String>();
		user2.put("IDInWorkday", "");
		user2.put("IDInSap", "2");
		user2.put("Email", "some.email.2@fakemail.com");
		user2.put("Name", "SomeName_2");
		user2.put("WorkerNameInWorkday", "");
		user2.put("UserNameInSap", "username_2_B");

		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("IDInWorkday", "1");
		user1.put("IDInSap", "1");
		user1.put("Email", "some.email.1@fakemail.com");
		user1.put("Name", "SomeName_1");
		user1.put("WorkerNameInWorkday", "username_1_A");
		user1.put("UserNameInSap", "username_1_B");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.add(user0);
		userList.add(user2);
		userList.add(user1);

		return userList;

	}

}
