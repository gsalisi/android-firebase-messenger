package ca.gsalisi.unimessenger;

class Message {
    private String user;
    private String msg;

    @SuppressWarnings("unused")
	private Message() {}

    public Message(String msg, String user) {
        this.user = user;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }
}