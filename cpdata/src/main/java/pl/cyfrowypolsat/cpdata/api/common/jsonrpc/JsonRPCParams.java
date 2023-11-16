package pl.cyfrowypolsat.cpdata.api.common.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import pl.cyfrowypolsat.cpdata.api.common.useragent.UserAgentData;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRPCParams {

    public JsonRPCParams() {

    }

    public JsonRPCParams(AuthData authData) {
        this.authData = authData;
    }

    @JsonIgnore
    public Message message;
    @JsonIgnore
    public String clientId;
    @JsonIgnore
    public AuthData authData;
    @JsonIgnore
    public UserAgentData userAgentData;

    public static class Message {
        final String timestamp;
        final String id;

        public Message(String timestamp, String id) {
            this.timestamp = timestamp;
            this.id = id;
        }
    }

    public static class AuthData {
        public final String sessionToken;

        public AuthData(String sessionToken) {
            this.sessionToken = sessionToken;
        }
    }
}
