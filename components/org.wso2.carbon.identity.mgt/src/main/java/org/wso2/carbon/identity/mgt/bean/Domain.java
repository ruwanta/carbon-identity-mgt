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

package org.wso2.carbon.identity.mgt.bean;

import org.wso2.carbon.identity.mgt.claim.MetaClaimMapping;
import org.wso2.carbon.identity.mgt.config.IdentityStoreConnectorConfig;
import org.wso2.carbon.identity.mgt.exception.DomainException;
import org.wso2.carbon.identity.mgt.exception.IdentityStoreException;
import org.wso2.carbon.identity.mgt.store.connector.CredentialStoreConnector;
import org.wso2.carbon.identity.mgt.store.connector.IdentityStoreConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a domain.
 */
public class Domain {

    /**
     * Mapping between IdentityStoreConnector ID and IdentityStoreConnector
     */
    private Map<String, IdentityStoreConnector> identityStoreConnectorsMap = new HashMap<>();

    /**
     * Mapping between CredentialStoreConnector ID and CredentialStoreConnector
     */
    private Map<String, CredentialStoreConnector> credentialStoreConnectorsMap = new HashMap<>();

    /**
     * Set of CredentialStoreConnectors for this domain sorted by their priority.
     */
    private SortedSet<CredentialStoreConnector> sortedCredentialStoreConnectors = new TreeSet<>(
            (c1, c2) -> {
                int c1Priority = c1.getCredentialStoreConfig().getPriority();
                int c2Priority = c2.getCredentialStoreConfig().getPriority();

                if (c1Priority == c2Priority) {
                    c2Priority++;
                }

                return Integer.compare(c1Priority, c2Priority);
            }
    );

    /**
     * Set of IdentityStoreConnectors for this domain sorted by their priority.
     */
    private SortedSet<IdentityStoreConnector> sortedIdentityStoreConnectors = new TreeSet<>(
            (c1, c2) -> {
                int c1Priority = c1.getIdentityStoreConfig().getPriority();
                int c2Priority = c2.getIdentityStoreConfig().getPriority();

                if (c1Priority == c2Priority) {
                    c2Priority++;
                }

                return Integer.compare(c1Priority, c2Priority);
            }
    );


    /**
     * Mapping between IdentityStoreConnector ID and MetaClaimMapping
     */
    private Map<String, List<MetaClaimMapping>> claimMappings = new HashMap<>();

    /**
     * Name of the domain.
     */
    private String domainName;

    /**
     * Priority of the domain.
     * Highest priority for domain is 1
     * Domain priority value should be greater than 0
     */
    private int domainPriority;

    public Domain(String domainName, int domainPriority) throws DomainException {

        if (domainPriority < 1) {
            throw new DomainException("Domain priority value should be greater than 0");
        }

        this.domainName = domainName;
        this.domainPriority = domainPriority;
    }

    /**
     * Get the domain name.
     *
     * @return String - domain name
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Get the priority of the domain.
     *
     * @return integer - domain priority
     */
    public int getDomainPriority() {

        return domainPriority;
    }

    /**
     * Add an identity store connector to the map.
     *
     * @param identityStoreConnector Identity Store connector
     */
    public void addIdentityStoreConnector(IdentityStoreConnector identityStoreConnector,
                                          IdentityStoreConnectorConfig identityStoreConnectorConfig)
            throws DomainException {

        String identityStoreConnectorId = identityStoreConnectorConfig.getConnectorId();

        if (identityStoreConnectorsMap.containsKey(identityStoreConnectorId)) {

            throw new DomainException(String
                    .format("IdentityStoreConnector %s already exists in the identity store connector map",
                            identityStoreConnectorId));
        }

        try {
            identityStoreConnector.init(identityStoreConnectorConfig);
            identityStoreConnectorsMap.put(identityStoreConnectorId, identityStoreConnector);
            sortedIdentityStoreConnectors.add(identityStoreConnector);
        } catch (IdentityStoreException e) {
            throw new DomainException("Error adding identity store to domain", e);
        }
    }

    /**
     * Get IdentityStoreConnector from identity store connector id.
     *
     * @param identityStoreConnectorId String - IdentityStoreConnectorId
     * @return IdentityStoreConnector
     */
    public IdentityStoreConnector getIdentityStoreConnectorFromId(String identityStoreConnectorId) {

        return identityStoreConnectorsMap.get(identityStoreConnectorId);
    }

    /**
     * Add an credential store connector to the map.
     *
     * @param credentialStoreConnector Credential Store connector
     * @throws DomainException domain exception
     */
    public void addCredentialStoreConnector(CredentialStoreConnector credentialStoreConnector)
            throws DomainException {

        String credentialStoreConnectorId = credentialStoreConnector.getCredentialStoreConnectorId();

        if (credentialStoreConnectorsMap.containsKey(credentialStoreConnectorId)) {

            throw new DomainException(String
                    .format("CredentialStoreConnector %s already exists in the credential store connector map",
                            credentialStoreConnectorId));
        }

        credentialStoreConnectorsMap.put(credentialStoreConnectorId, credentialStoreConnector);
        sortedCredentialStoreConnectors.add(credentialStoreConnector);
    }

    /**
     * Get CredentialStoreConnector from credential store connector id.
     *
     * @param credentialStoreConnectorId String - CredentialStoreConnector ID
     * @return credentialStoreConnector
     */
    public CredentialStoreConnector getCredentialStoreConnectorFromId(String credentialStoreConnectorId) {

        return credentialStoreConnectorsMap.get(credentialStoreConnectorId);
    }

    /**
     * Checks weather a certain claim URI exists in the domain claim mappings.
     *
     * @param claimURI Claim
     * @return is claim belong to domain
     */
    public boolean isClaimAvailable(String claimURI) {

        return claimMappings.values().stream()
                .anyMatch(list -> list.stream().filter(metaClaimMapping ->
                        claimURI.equals(metaClaimMapping.getMetaClaim().getClaimURI()))
                        .findFirst().isPresent());
    }

    /**
     * Get claim mappings for an identity store id.
     *
     * @return Map of connector Id to List of MetaClaimMapping
     */
    public Map<String, List<MetaClaimMapping>> getClaimMappings() {

        return claimMappings;
    }

    /**
     * Set claim mappings for an identity store id.
     *
     * @param claimMappings Map<String, List<MetaClaimMapping>> claim mappings
     */
    public void setClaimMappings(Map<String, List<MetaClaimMapping>> claimMappings) {

        this.claimMappings = claimMappings;
    }

    /**
     * Get set of IdentityStoreConnectors for this domain sorted by their priority.
     *
     * @return Sorted IdentityStoreConnectors set
     */
    public SortedSet<IdentityStoreConnector> getSortedIdentityStoreConnectors() {
        return sortedIdentityStoreConnectors;
    }

    /**
     * Get set of CredentialStoreConnectors for this domain sorted by their priority.
     *
     * @return Sorted CredentialStoreConnectors set
     */
    public SortedSet<CredentialStoreConnector> getSortedCredentialStoreConnectors() {
        return sortedCredentialStoreConnectors;
    }
}
