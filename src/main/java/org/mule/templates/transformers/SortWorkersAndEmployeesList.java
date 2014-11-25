/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two.
 * The identity of an element of the list is defined by its email.
 *
 * @author aurel.medvegy
 */
public class SortWorkersAndEmployeesList extends AbstractMessageTransformer {

    public static Comparator<Map<String, String>> recordComparator = new Comparator<Map<String, String>>() {

        public int compare(Map<String, String> user1, Map<String, String> user2) {

            String key1 = buildKey(user1);
            String key2 = buildKey(user2);

            return key1.compareTo(key2);

        }

        private String buildKey(Map<String, String> user) {
            StringBuilder key = new StringBuilder();

            if (StringUtils.isNotBlank(user.get("IDInWorkday")) && StringUtils.isNotBlank(user.get("IDInSap"))) {
                key.append("~~");
                key.append(user.get("IDInWorkday"));
                key.append(user.get("IDInSap"));
                key.append(user.get("Email"));
            }

            if (StringUtils.isNotBlank(user.get("IDInWorkday")) && StringUtils.isBlank(user.get("IDInSap"))) {
                key.append(user.get("IDInWorkday"));
                key.append("~");
                key.append(user.get("Email"));
            }

            if (StringUtils.isBlank(user.get("IDInWorkday")) && StringUtils.isNotBlank(user.get("IDInSap"))) {
                key.append("~");
                key.append(user.get("IDInSap"));
                key.append(user.get("Email"));
            }

            return key.toString();
        }

    };

    @SuppressWarnings("unchecked")
    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

        List<Map<String, String>> sortedUsersList = (List<Map<String, String>>) message.getPayload();

        Collections.sort(sortedUsersList, recordComparator);

        return sortedUsersList;
    }
}
