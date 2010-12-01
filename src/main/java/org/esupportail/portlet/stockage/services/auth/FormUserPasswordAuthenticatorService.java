/**
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * Copyright (C) 2010 UNR RUNN http://www.unr-runn.fr
 * @Author (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.esupportail.portlet.stockage.services.auth;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portlet.stockage.beans.SharedUserPortletParameters;

public class FormUserPasswordAuthenticatorService extends UserPasswordAuthenticatorService {

	protected static final Log log = LogFactory.getLog(FormUserPasswordAuthenticatorService.class);

	protected String userInfo4Username;
	
	/**
	 * To set a default username retrieving from user uPortal attributes
	 * @param userInfo4Username
	 */
	public void setUserInfo4Username(String userInfo4Username) {
		this.userInfo4Username = userInfo4Username;
	}

	public void initialize(Map userInfos, SharedUserPortletParameters userParameters) {
		if(userInfo4Username != null && userInfos != null && userInfos.containsKey(userInfo4Username)) {
			this.setUsername((String)userInfos.get(userInfo4Username));
		}
	}
	
}