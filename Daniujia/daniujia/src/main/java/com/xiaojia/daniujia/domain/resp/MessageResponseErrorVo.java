package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/6.
 */
public class MessageResponseErrorVo {

    private ErrorInfo error;
    private RequestParams requestParams;

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public RequestParams getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    public static class ErrorInfo {
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class RequestParams {

        private MessageEntity message;

        public MessageEntity getMessage() {
            return message;
        }

        public void setMessage(MessageEntity message) {
            this.message = message;
        }

        public static class MessageEntity {
            private String to;
            private String code;
            private String from;

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }
        }
    }
}
