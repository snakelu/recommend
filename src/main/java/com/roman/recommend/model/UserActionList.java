package com.roman.recommend.model;

import java.io.Serializable;
import java.util.List;

import com.roman.recommend.entity.UserAction;

/**
 * {@link}UserAction list
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
public class UserActionList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UserAction> userActions;

	public List<UserAction> getUserActions() {
		return userActions;
	}

	public void setUserActions(List<UserAction> userActions) {
		this.userActions = userActions;
	}

}
