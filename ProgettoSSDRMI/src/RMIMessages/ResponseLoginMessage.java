package RMIMessages;

import java.util.ArrayList;

import chat.Contact;
import utility.RMIResponse;

public class ResponseLoginMessage extends RMISIPBasicResponseMessage{

	private Contact contact;							//Informazioni sul proprio contatto
	private ArrayList<Contact> contactList = null;		//Informazioni sui propri contatti amici
	
	
	
	public ResponseLoginMessage(boolean success, String message, Contact contact, ArrayList<Contact> contactList){
		super(success,message);
		this.contact=contact;
	}
	public ResponseLoginMessage(boolean success, String message, Contact contact){
		super(success,message);
		this.contact=contact;
	}
	public Contact getLoggedContact() {
		return contact;
	}
	public void setLoggedContact(Contact contact) {
		this.contact = contact;
	}
	public ArrayList<Contact> getContactList() {
		return contactList;
	}
	public void setContactList(ArrayList<Contact> contactList) {
		this.contactList = contactList;
	}

}
