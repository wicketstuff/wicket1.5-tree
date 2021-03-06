/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.extensions.markup.html.tree;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class TreeTablePageTest {
	private WicketTester tester;
	private TreeTablePage page;

	@Before
	public void setup() {
		tester = new WicketTester();
	}

	@Test
	public void pageRenders() {
		page = tester.startPage(TreeTablePage.class);
		tester.assertRenderedPage(TreeTablePage.class);
	}

	@Test
	public void expandOpaNodeRendersChildrenUsingAjax() {
		pageRenders();
		
		// expand the first node in the tree table (the Opa node)
		tester.clickLink("tree:i:0:sideBodyColumns:0:comp:link");
		MockHttpServletResponse opaLinkResponse = tester.getLastResponse();

		final String opaResponseBody = opaLinkResponse.getDocument();

		// assert that all rendered AJAX links their client side event handlers are registered 
		page.visitChildren(AjaxLink.class, new IVisitor<AjaxLink, Void>() {
			@Override
			public void component(AjaxLink ajaxLink, IVisit<Void> iVisit) {
				assertThat(opaResponseBody, containsString("\"c\":\"" + ajaxLink.getMarkupId() + "\""));
			}
		});

//		tester.clickLink("tree:i:2:sideBodyColumns:0:comp:link");
//		MockHttpServletResponse childLinkResponse = tester.getLastResponse();
//
//		String childResponseBody = childLinkResponse.getDocument();
//		page.visitChildren(AjaxLink.class, (v, r) -> {
//			assertThat(childResponseBody, containsString("\"c\":\"" + v.getMarkupId() + "\""));
//		});
	}
}
