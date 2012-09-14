package client;

import java.util.ArrayList;

import chat.Message;

public class INChatMessageListManager {

	private static ArrayList<Message> IN_ChatMessageList = new ArrayList<Message>();
	
	public static synchronized void addReceivedMsg(Message chatMsg){
		IN_ChatMessageList.add(chatMsg);
	}
}
