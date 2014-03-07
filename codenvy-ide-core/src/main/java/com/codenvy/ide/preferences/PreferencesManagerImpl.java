/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.preferences;

import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.api.user.gwt.client.UserProfileServiceClient;
import com.codenvy.api.user.gwt.client.UserServiceClient;
import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link PreferencesManager}.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@Singleton
public class PreferencesManagerImpl implements PreferencesManager {
    private Map<String, String>      persistedPreferences;
    private Map<String, String>      changedPreferences;
    private UserProfileServiceClient userProfileService;

    /**
     * Create preferences.
     *
     * @param userService
     */
    @Inject
    protected PreferencesManagerImpl(UserProfileServiceClient userProfileService) {
        this.persistedPreferences = new HashMap<String, String>();
        this.changedPreferences = new HashMap<String, String>();
        this.userProfileService = userProfileService;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(String preferenceName) {
        boolean isValueDeleted = changedPreferences.containsKey(preferenceName);
        String value = changedPreferences.get(preferenceName);
        if (value == null && !isValueDeleted) {
            value = persistedPreferences.get(preferenceName);
        }

        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void setPreference(String name, String value) {
        changedPreferences.put(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void removeValue(String name) {
        setPreference(name, null);
    }

    /** {@inheritDoc} */
    @Override
    public void flushPreferences(final AsyncCallback<Profile> callback) {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.putAll(changedPreferences);

       userProfileService.updatePreferences(attributes, new AsyncRequestCallback<Profile>() {
            @Override
            protected void onSuccess(Profile result) {
                persistedPreferences.putAll(changedPreferences);
                changedPreferences.clear();
                callback.onSuccess(result);
            }

            @Override
            protected void onFailure(Throwable exception) {
                callback.onFailure(exception);
                Log.error(PreferencesManagerImpl.class, exception);
            }
        });
    }

    /**
     * Reads preferences from input map.
     *
     * @param preferences
     */
    public void load(Map<String, String> preferences) {
        persistedPreferences.putAll(preferences);
    }
}