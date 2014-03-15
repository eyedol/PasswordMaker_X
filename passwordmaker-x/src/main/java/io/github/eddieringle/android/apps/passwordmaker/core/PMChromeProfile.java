package io.github.eddieringle.android.apps.passwordmaker.core;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model for PM chrome extension
 */
public class PMChromeProfile implements Serializable {

    @SerializedName("<RDF>Description")
    public List<Profile> mProfileList;

    @Override
    public String toString() {
        return "PMChromeProfile{" +
                "mProfileList=" + mProfileList +
                '}';
    }

    public static class Profile implements Serializable {

        @SerializedName("@<NS1>domainCB")
        private Boolean mUseUrlDomain;

        @SerializedName("@<NS1>pathCB")
        private Boolean mUseUrlOther;

        @SerializedName("@<NS1>protocolCB")
        private Boolean mUseUrlProtocol;

        @SerializedName("@<NS1>subdomainCB")
        private Boolean mUseUrlSubdomain;

        @SerializedName("@<NS1>leetLevelLB")
        private Integer mL33tLevel;

        @SerializedName("@<NS1>passwordLength")
        private Integer mPasswordLength;

        @SerializedName("@<NS1>charset")
        private String mCharacterSet;

        @SerializedName("@<NS1>hashAlgorithmLB")
        private String mHashAlgorithm;

        @SerializedName("@<NS1>whereLeetLB")
        private String mL33tOrder;

        @SerializedName("@<NS1>counter")
        private String mModifier;

        @SerializedName("@<NS1>prefix")
        private String mPasswordPrefix;

        @SerializedName("@<NS1>suffix")
        private String mPasswordSuffix;

        @SerializedName("@<NS1>name")
        public String mProfileName;

        @SerializedName("@<NS1>usernameTB")
        private String mUsername;

        public Boolean getUseUrlDomain() {
            return mUseUrlDomain;
        }

        public void setUseUrlDomain(Boolean useUrlDomain) {
            mUseUrlDomain = useUrlDomain;
        }

        public Boolean getUseUrlOther() {
            return mUseUrlOther;
        }

        public void setUseUrlOther(Boolean useUrlOther) {
            mUseUrlOther = useUrlOther;
        }

        public Boolean getUseUrlProtocol() {
            return mUseUrlProtocol;
        }

        public void setUseUrlProtocol(Boolean useUrlProtocol) {
            mUseUrlProtocol = useUrlProtocol;
        }

        public Boolean getUseUrlSubdomain() {
            return mUseUrlSubdomain;
        }

        public void setUseUrlSubdomain(Boolean useUrlSubdomain) {
            mUseUrlSubdomain = useUrlSubdomain;
        }

        public Integer getL33tLevel() {
            return mL33tLevel;
        }

        public void setL33tLevel(Integer l33tLevel) {
            mL33tLevel = l33tLevel;
        }

        public Integer getPasswordLength() {
            return mPasswordLength;
        }

        public void setPasswordLength(Integer passwordLength) {
            mPasswordLength = passwordLength;
        }

        public String getCharacterSet() {
            return mCharacterSet;
        }

        public void setCharacterSet(String characterSet) {
            mCharacterSet = characterSet;
        }

        public String getHashAlgorithm() {
            return mHashAlgorithm;
        }

        public void setHashAlgorithm(String hashAlgorithm) {
            mHashAlgorithm = hashAlgorithm;
        }

        public String getL33tOrder() {
            return mL33tOrder;
        }

        public void setL33tOrder(String l33tOrder) {
            mL33tOrder = l33tOrder;
        }

        public String getModifier() {
            return mModifier;
        }

        public void setModifier(String modifier) {
            mModifier = modifier;
        }

        public String getPasswordPrefix() {
            return mPasswordPrefix;
        }

        public void setPasswordPrefix(String passwordPrefix) {
            mPasswordPrefix = passwordPrefix;
        }

        public String getPasswordSuffix() {
            return mPasswordSuffix;
        }

        public void setPasswordSuffix(String passwordSuffix) {
            mPasswordSuffix = passwordSuffix;
        }

        public String getProfileName() {
            return mProfileName;
        }

        public void setProfileName(String profileName) {
            mProfileName = profileName;
        }

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String username) {
            mUsername = username;
        }

        @Override
        public String toString() {
            return "PMChromeProfile{" +
                    "mUseUrlDomain=" + mUseUrlDomain +
                    ", mUseUrlOther=" + mUseUrlOther +
                    ", mUseUrlProtocol=" + mUseUrlProtocol +
                    ", mUseUrlSubdomain=" + mUseUrlSubdomain +
                    ", mL33tLevel=" + mL33tLevel +
                    ", mPasswordLength=" + mPasswordLength +
                    ", mCharacterSet='" + mCharacterSet + '\'' +
                    ", mHashAlgorithm='" + mHashAlgorithm + '\'' +
                    ", mL33tOrder='" + mL33tOrder + '\'' +
                    ", mModifier='" + mModifier + '\'' +
                    ", mPasswordPrefix='" + mPasswordPrefix + '\'' +
                    ", mPasswordSuffix='" + mPasswordSuffix + '\'' +
                    ", mProfileName='" + mProfileName + '\'' +
                    ", mUsername='" + mUsername + '\'' +
                    '}';
        }
    }
}
