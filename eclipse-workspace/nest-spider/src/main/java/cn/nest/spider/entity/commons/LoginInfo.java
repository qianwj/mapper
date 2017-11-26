package cn.nest.spider.entity.commons;

import java.util.Map;

public class LoginInfo {

	private Map<String, String> initHeaders;
	
	private String usernameXPath;
	
	private String passwordXPath;
	
	private String clickXPath;
	
	private String username;
	
	private String password;
	
	private String loginUrl;
	
	private String user_agent;
	
	private int timeout = 5000;

	public Map<String, String> getInitHeaders() {
		return initHeaders;
	}

	public LoginInfo setInitHeaders(Map<String, String> initHeaders) {
		this.initHeaders = initHeaders;
		return this;
	}

	public String getUsernameXPath() {
		return usernameXPath;
	}

	public LoginInfo setUsernameXPath(String usernameXPath) {
		this.usernameXPath = usernameXPath;
		return this;
	}

	public String getPasswordXPath() {
		return passwordXPath;
	}

	public LoginInfo setPasswordXPath(String passwordXPath) {
		this.passwordXPath = passwordXPath;
		return this;
	}

	public String getClickXPath() {
		return clickXPath;
	}

	public LoginInfo setClickXPath(String clickXPath) {
		this.clickXPath = clickXPath;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public LoginInfo setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public LoginInfo setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public LoginInfo setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
		return this;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public LoginInfo setUser_agent(String user_agent) {
		this.user_agent = user_agent;
		return this;
	}

	public int getTimeout() {
		return timeout;
	}

	public LoginInfo setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	public String toString() {
		return "    {\"initHeaders\":\"" + initHeaders + "\",\"usernameXPath\":\"" + usernameXPath
				+ "\",\"passwordXPath\":\"" + passwordXPath + "\",\"clickXPath\":\"" + clickXPath + "\",\"username\":\""
				+ username + "\",\"password\":\"" + password + "\",\"loginUrl\":\"" + loginUrl + "\",\"user_agent\":\""
				+ user_agent + "\",\"timeout\":\"" + timeout + "\"}  ";
	}
}
