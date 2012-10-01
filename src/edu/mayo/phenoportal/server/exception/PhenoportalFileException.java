package edu.mayo.phenoportal.server.exception;

public class PhenoportalFileException extends Exception {

    private static final long serialVersionUID = 4029947500046788141L;

    public PhenoportalFileException(Exception e) {
        this.setStackTrace(e.getStackTrace());
    }

}
