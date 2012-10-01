package edu.mayo.phenoportal.client.upload;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {

    void retrieveUploadMetadata(String parentId, String fileName, String version,
            AsyncCallback<ClientUploadItems> asyncCallback);

}
