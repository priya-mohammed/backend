
package com.spantag.socialMediaAppln.login;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final long userID;

	// private final List<UserMenu> menu;

	public long getUserID() {

		return userID;
	}

	/*
	 * public List<UserMenu> getMenu() { return menu; }
	 */

	public CustomUser(String userName, String password, long userID, Collection<? extends GrantedAuthority> authorities) {

		super(userName, password, authorities);
		this.userID = userID;

		// this.menu = menu;
	}
}
