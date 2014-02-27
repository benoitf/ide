package com.codenvy.ide.factory.shared;

/**
 * @author vzhukovskii@codenvy.com
 */
public enum Param {
    ID("id"),
    V("v"),
    VCS("vcs"),
    VCS_URL("vcsurl"),
    COMMIT_ID("commitid"),
    VCS_INFO("vcsinfo"),
    VCS_BRANCH("vcsbranch"),
    OPEN_FILE("openfile"),
    ACTION("action"),
    WS_NAME("wname"),
    ORG_ID("orgid"),
    AFFILIATE_ID("affiliateid"),
    PROFILE_ATTRIBUTES("projectattributes"),
    PROFILE_ATTRIBUTES_PNAME("pname"),
    PROFILE_ATTRIBUTES_PTYPE("ptype"),
    STYLE("style"),
    DESCRIPTION("description"),
    CONTACT_MAIL("contactmail"),
    AUTHOR("author"),
    VARIABLES("variables");

    private final String value;

    private Param(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Param fromValue(String value) {
        for (Param v : Param.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid value '" + value + "' ");
    }
}
