/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.image;
import java.util.List;

/**
 * Auto-generated: 2018-02-11 20:57:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Site {

    private String site_id;
    private String type;
    private String name;
    private String domain;
    private String description;
    private int followers;
    private String url;
    private String icon;
    private boolean verified;
    private int verified_type;
    private String verified_reason;
    private int verifications;
    private List<String> verification_list;
    private boolean is_following;
    public void setSite_id(String site_id) {
         this.site_id = site_id;
     }
     public String getSite_id() {
         return site_id;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setDomain(String domain) {
         this.domain = domain;
     }
     public String getDomain() {
         return domain;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setFollowers(int followers) {
         this.followers = followers;
     }
     public int getFollowers() {
         return followers;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public void setVerified(boolean verified) {
         this.verified = verified;
     }
     public boolean getVerified() {
         return verified;
     }

    public void setVerified_type(int verified_type) {
         this.verified_type = verified_type;
     }
     public int getVerified_type() {
         return verified_type;
     }

    public void setVerified_reason(String verified_reason) {
         this.verified_reason = verified_reason;
     }
     public String getVerified_reason() {
         return verified_reason;
     }

    public void setVerifications(int verifications) {
         this.verifications = verifications;
     }
     public int getVerifications() {
         return verifications;
     }

    public void setVerification_list(List<String> verification_list) {
         this.verification_list = verification_list;
     }
     public List<String> getVerification_list() {
         return verification_list;
     }

    public void setIs_following(boolean is_following) {
         this.is_following = is_following;
     }
     public boolean getIs_following() {
         return is_following;
     }

}