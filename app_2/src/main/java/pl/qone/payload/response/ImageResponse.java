package pl.qone.payload.response;

public class ImageResponse {
	
	private String message;
	private String imageId;
	
	public ImageResponse(String message, String imageId) {
		this.message = message;
		this.imageId = imageId;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	

}
