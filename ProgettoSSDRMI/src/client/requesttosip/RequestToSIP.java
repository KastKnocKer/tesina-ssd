package client.requesttosip;

public class RequestToSIP {
	private RequestToSIPTypeList RequestType;
	private Object RequestMessage;
	private int ProgressiveNum;
	
	public RequestToSIP(RequestToSIPTypeList RequestType, Object RequestMessage){
		this.RequestType = RequestType;
		this.RequestMessage = RequestMessage;
	}

	public RequestToSIPTypeList getRequestType() {
		return RequestType;
	}

	public void setRequestType(RequestToSIPTypeList requestType) {
		RequestType = requestType;
	}

	public Object getRequestMessage() {
		return RequestMessage;
	}

	public void setRequestMessage(Object requestMessage) {
		RequestMessage = requestMessage;
	}

	public int getProgressiveNum() {
		return ProgressiveNum;
	}

	public void setProgressiveNum(int progressiveNum) {
		ProgressiveNum = progressiveNum;
	}
	
}
