package com.eip.festevent.beans;

import com.eip.festevent.authentication.TokenHelper;
import com.eip.festevent.dao.morphia.QueriesAllowed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;
import java.util.List;

@Entity
@QueriesAllowed(fields = {"email", "lastName", "firstName"}, operators = {"contains", "=", "order", "limit", "offset"})
public class User {
	
	@Id
	protected String id = ObjectId.get().toString();

	protected String accessToken;

	@JsonIgnore
	protected String password;
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
	protected String role = "STANDARD";

	protected String  email;

	protected String lastName;
	protected String firstName;
	protected int		friendsNumber = 0;
	protected String  phone;
	protected Date birthdate;
	protected Media profilPicture;

	@JsonIgnore
	protected List<FriendRequest> friendsRequestsSent = Lists.newArrayList();
	@JsonIgnore
	protected List<FriendRequest> friendsRequestsReceived = Lists.newArrayList();

	@JsonIgnore
	protected List<String> friends = Lists.newArrayList();

	@JsonIgnore
	@Embedded
	protected List<Media> pictures;

	@JsonIgnore
	protected double  locationLongitude;
	@JsonIgnore
	protected double  locationLatitude;

	public int getFriendsNumber() {
		return friendsNumber;
	}

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
		friendsNumber++;
	}

	public void removeFriend(String email) {
		friends.remove(email);
		friendsNumber--;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void signIn() {
		accessToken = TokenHelper.generateToken(email);
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
