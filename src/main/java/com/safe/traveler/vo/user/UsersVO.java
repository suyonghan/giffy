package com.safe.traveler.vo.user;

public class UsersVO {

	private int userNo;
	private String email;
	private String password;
	private String name;
	private String nickname;
	private String birthdate;
	private String job;
	private String sex;
	private String phone;


	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "UsersVO [user_no=" + userNo + ", password=" + password + ", name=" + name + ", nickname=" + nickname
				+ ", birthdate=" + birthdate + ", job=" + job + ", email=" + email + ", sex=" + sex + ", phone=" + phone
				+ "]";
	}

}
