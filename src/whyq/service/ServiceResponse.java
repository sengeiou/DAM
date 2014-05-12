package whyq.service;

public class ServiceResponse {
    private ServiceAction _action;
    private ResultCode _code;
    private Object _data;

    public ServiceResponse(ServiceAction action, Object data, ResultCode code) {
        _action = action;
        _code = code;
        _data = data;
    }

    public ServiceResponse(ServiceAction action, Object data) {
        this(action, data, ResultCode.Success);
    }

    public boolean isSuccess() {
        return (_code == ResultCode.Success);
    }

    public ServiceAction getAction() {
        return _action;
    }

    public ResultCode getCode() {
        return _code;
    }

    public Object getData() {
        return _data;
    }
}
