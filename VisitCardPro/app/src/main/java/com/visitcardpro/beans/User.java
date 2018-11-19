package com.visitcardpro.beans;

import com.beust.jcommander.internal.Lists;

import java.util.Date;
import java.util.List;

public class User {

	protected String accessToken;


	protected String role;

	protected String  email;

	protected String lastName;
	protected String firstName;

	protected String  phone;
	protected Date birthdate;
	protected Media profilPicture;

	protected List<FriendRequest> friendsRequestsSent = Lists.newArrayList();
	protected List<FriendRequest> friendsRequestsReceived = Lists.newArrayList();

	protected List<String> friends = Lists.newArrayList();

	protected List<Media> pictures;

	protected double  locationLongitude;
	protected double  locationLatitude;

	public void addPicture(Media media) {
		pictures.add(media);
	}

	public void removePicture(Media media) {
		pictures.remove(media);
	}

	public List<Media> getPictures() {
		return pictures;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Media getProfilPicture() {
		return profilPicture;
	}

	public void setProfilPicture(Media profilPicture) {
		this.profilPicture = profilPicture;
	}

	public double getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(double locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

	public double getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(double locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public void addFriend(String email) {
		friends.add(email);
	}

	public void removeFriend(String email) {
		friends.remove(email);
	}

	public List<String> getFriends() {
		return friends;
	}

	public void signOut() {
		accessToken = "";
	}

	public void addFriendRequestSent(String login) {
		friendsRequestsSent.add(new FriendRequest(email, login, null));
	}

	public void addFriendRequestReceived(String login) {
		friendsRequestsReceived.add(new FriendRequest(login, email, null));
	}

	public boolean rmFriendRequestSent(String login) {
		for (FriendRequest fr : friendsRequestsSent) {
			if (fr.getTarget().equals(login)) {
				return (friendsRequestsSent.remove(fr));
			}
		}
		return false;
	}

	public boolean rmFriendRequestReceived(String login) {
		for (FriendRequest fr : friendsRequestsReceived) {
			if (fr.getSender().equals(login)) {
				return friendsRequestsReceived.remove(fr);
			}
		}
		return false;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<FriendRequest> getFriendsRequestsSent() {
		return friendsRequestsSent;
	}

	public List<FriendRequest> getFriendsRequestsReceived() {
		return friendsRequestsReceived;
	}

}
