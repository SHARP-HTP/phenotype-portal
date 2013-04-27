package edu.mayo.phenoportal.client.upload;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RPCFileService")
public interface FileService extends RemoteService {

    ClientUploadItems retrieveUploadMetadata(int id);

}
