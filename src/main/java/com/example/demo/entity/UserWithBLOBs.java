package com.example.demo.entity;

public class UserWithBLOBs extends User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_user.discription
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    private String discription;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_user.user_info
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    private String userInfo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_user.discription
     *
     * @return the value of tbl_user.discription
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_user.discription
     *
     * @param discription the value for tbl_user.discription
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    public void setDiscription(String discription) {
        this.discription = discription == null ? null : discription.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_user.user_info
     *
     * @return the value of tbl_user.user_info
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    public String getUserInfo() {
        return userInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_user.user_info
     *
     * @param userInfo the value for tbl_user.user_info
     *
     * @mbg.generated Thu Jun 27 15:10:32 CST 2024
     */
    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo == null ? null : userInfo.trim();
    }
}
