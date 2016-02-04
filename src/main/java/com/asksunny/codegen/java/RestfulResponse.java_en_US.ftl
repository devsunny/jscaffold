package ${config.domainPackageName};

public class RestfulResponse<Type> {

	private String message;
	private int statusCode;
	private Type payload;

	public RestfulResponse() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Type getPayload() {
		return payload;
	}

	public void setPayload(Type payload) {
		this.payload = payload;
	}

}
