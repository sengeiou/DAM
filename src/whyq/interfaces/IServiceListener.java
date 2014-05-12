package whyq.interfaces;

import whyq.service.Service;
import whyq.service.ServiceResponse;

public interface IServiceListener {
    /*
     * Called when a request has been fisnished without canceled.
     */
    public void onCompleted(Service service, ServiceResponse result);
}
