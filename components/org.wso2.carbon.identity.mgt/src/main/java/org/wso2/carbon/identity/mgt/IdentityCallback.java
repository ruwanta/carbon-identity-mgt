/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.mgt;

import java.io.Serializable;
import javax.security.auth.callback.Callback;

/**
 * <p>
 * The {@code Callback} is an implementation of the {@code Callback} class.
 * Underlying security services instantiate and pass a
 * {@code NameCallback} to the {@code handle}
 * method of a {@code CallbackHandler} to retrieve HttpRequest information.
 * </p>
 *
 * @param <T> This describes the type handled by the callback
 * @since 1.0.0
 */
public class IdentityCallback<T> implements Callback, Serializable {

    private static final long serialVersionUID = 6056209529374750070L;

    private transient T content;

    private String loginModuleType;

    public IdentityCallback(String loginModuleType) {
        this.loginModuleType = loginModuleType;
    }

    public T getContent() {

        return content;
    }

    public void setContent(T content) {

        this.content = content;
    }

    public String getLoginModuleType() {
        return this.loginModuleType;
    }

}
