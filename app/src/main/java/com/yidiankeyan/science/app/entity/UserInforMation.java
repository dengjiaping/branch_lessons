package com.yidiankeyan.science.app.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/8 0008.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class UserInforMation {
    /**
     * userid : e957c2101d4b41f6a75b16347d3a35bc
     * birthday : null
     * username : null
     * userimgurl : null
     * sizeid : 25994074
     * profession : null
     * position : null
     * school : null
     * phone : 18500560145
     * email : null
     * address : null
     * isBoundQQ : 0
     * isBoundWeChat : 0
     * isBountWeiBo : 0
     * gender : 0
     * saisiProfession : 0
     * specialty : null
     */

    private String userid;
    private String birthday;
    private String username;
    private String userimgurl;
    private String bgimgurl;
    private String sizeid;
    private String profession;
    private String degree;
    private String position;
    private String school;
    private String phone;
    private String email;
    private String address;
    private int isBoundQQ;
    private int isBoundWeChat;
    private int isBountWeiBo;
    private int gender;
    private int saisiProfession;
    private String specialty;
    /**
     * isuse : 1
     * isfocus : 0
     * focusnum : 0
     * authentication : 1
     * followernum : 0
     */

    private int isuse;
    private int isfocus;
    private int focusnum;
    private int authentication;
    private int followernum;
    /**
     * mysign : null
     */
    private String token;


    private String mysign;
    private int loginMode;
    private int isBinding;
    private String bindingPhone;
    /**
     * sig : eJxljkFPgzAAhe-8CtIrxrS0ddTEAyqbEOcMY6JeCKMt6eaAQKtsxv*u4hIxvuv35b33btm2DZLb5WleFLWpdKb3jQD2uQ0gOPmFTaN4lusMt-wfFH2jWpHlUot2gIhS6kI4dhQXlVZSHY0dpQgjzyUjpePbbNj56SBfBejMw38UVQ5wHqyuwmkcefySQ7z0NmSVGGmmb2QB7*FN7xSJnzuOPFyb1GAlfRX4L*5zD9Mnud0-YJ2v7*KCHrp6MgsqUq6jcpE*lmG8mc8iHF6MJrXaieMhNmGUMMZG9FW0naqrQXAhosjF8DvA*rA*AUprXJs_
     */

    private String sig;

    public String getBindingPhone() {
        return bindingPhone;
    }

    public void setBindingPhone(String bindingPhone) {
        this.bindingPhone = bindingPhone;
    }

    public int getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(int loginMode) {
        this.loginMode = loginMode;
    }

    public int getIsBinding() {
        return isBinding;
    }

    public void setIsBinding(int isBinding) {
        this.isBinding = isBinding;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBgimgurl() {
        return bgimgurl;
    }

    public void setBgimgurl(String bgimgurl) {
        this.bgimgurl = bgimgurl;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimgurl() {
        return userimgurl;
    }

    public void setUserimgurl(String userimgurl) {
        this.userimgurl = userimgurl;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsBoundQQ() {
        return isBoundQQ;
    }

    public void setIsBoundQQ(int isBoundQQ) {
        this.isBoundQQ = isBoundQQ;
    }

    public int getIsBoundWeChat() {
        return isBoundWeChat;
    }

    public void setIsBoundWeChat(int isBoundWeChat) {
        this.isBoundWeChat = isBoundWeChat;
    }

    public int getIsBountWeiBo() {
        return isBountWeiBo;
    }

    public void setIsBountWeiBo(int isBountWeiBo) {
        this.isBountWeiBo = isBountWeiBo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getSaisiProfession() {
        return saisiProfession;
    }

    public void setSaisiProfession(int saisiProfession) {
        this.saisiProfession = saisiProfession;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public int getIsfocus() {
        return isfocus;
    }

    public void setIsfocus(int isfocus) {
        this.isfocus = isfocus;
    }

    public int getFocusnum() {
        return focusnum;
    }

    public void setFocusnum(int focusnum) {
        this.focusnum = focusnum;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public int getFollowernum() {
        return followernum;
    }

    public void setFollowernum(int followernum) {
        this.followernum = followernum;
    }

    public String getMysign() {
        return mysign;
    }

    public void setMysign(String mysign) {
        this.mysign = mysign;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }
}
