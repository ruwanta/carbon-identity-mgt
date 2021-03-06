/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.mgt.store;

import org.wso2.carbon.identity.mgt.context.AuthenticationContext;
import org.wso2.carbon.identity.mgt.domain.DomainManager;
import org.wso2.carbon.identity.mgt.exception.AuthenticationFailure;
import org.wso2.carbon.identity.mgt.exception.CredentialStoreException;

import javax.security.auth.callback.Callback;

/**
 * Represents a virtual credential store to abstract the underlying stores.
 *
 * @since 1.0.0
 */
public interface CredentialStore {

    /**
     * Initialize credential store.
     *
     * @param domainManager              DomainManager instance for which is shared by the identity store
     *                                   and the credentials store.
     * @throws CredentialStoreException Credential Store Exception.
     */
    void init(DomainManager domainManager)
            throws CredentialStoreException;

    /**
     * Authenticate the user.
     *
     * @param callbacks Callbacks to get the user details.
     * @return If the authentication is success. AuthenticationFailure otherwise.
     * @throws AuthenticationFailure Authentication Failure.
     */
    AuthenticationContext authenticate(Callback[] callbacks) throws AuthenticationFailure;

    /**
     * Updates the credential of user.
     *
     * @param callbacks Callbacks to get user credentials.
     * @throws CredentialStoreException Credential store exception.
     */
    void updateCredential(Callback[] callbacks) throws CredentialStoreException;

    /**
     * Updates the credential of user.
     *
     * @param username            Username of the user.
     * @param credentialCallbacks Callbacks to get user credentials.
     * @param identityStoreId     Id of the identity store user resides.
     * @throws CredentialStoreException Credential store exception.
     */
    void updateCredential(String username, Callback[] credentialCallbacks, String identityStoreId) throws
            CredentialStoreException;
}
