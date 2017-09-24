package pers.platform.blog.model;

/**
 * 博主实体
 * 
 * @author Administrator
 *
 */
public class Blogger {

    private String id; // 编号
    private String userName; // 账号
    private String password; // 密码
    private String profile; // 个人简介
    private String nickName; // 昵称
    private String sign; // 个性签名
    private String imageName; // 图片路径
    private String personalized; // 我为自己代言

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPersonalized() {
        return personalized;
    }

    public void setPersonalized(String personalized) {
        this.personalized = personalized;
    }

}
