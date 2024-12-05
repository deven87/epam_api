package payload;

/**
 * PostResourcePayload POJO with Builder
 */
public class PostResourcePayload {

	int userId;
	String title;
	String body;

	private PostResourcePayload(PostResourcePayloadBuilder builder) {
		this.userId = builder.userId;
		this.title = builder.title;
		this.body = builder.body;
	}

	public int getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public static class PostResourcePayloadBuilder {

		int userId;
		String title;
		String body;

		public PostResourcePayloadBuilder setUserId(int userId) {
			this.userId = userId;
			return this;
		}

		public PostResourcePayloadBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public PostResourcePayloadBuilder setBody(String body) {
			this.body = body;
			return this;
		}

		public PostResourcePayload build() {
			return new PostResourcePayload(this);
		}
	}
}
