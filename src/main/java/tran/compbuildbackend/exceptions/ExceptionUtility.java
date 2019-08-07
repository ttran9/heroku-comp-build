package tran.compbuildbackend.exceptions;

import tran.compbuildbackend.exceptions.request.GenericRequestException;

public class ExceptionUtility {

    public static void throwUsernameException(String exceptionMessage) {
        GenericRequestException exception = new GenericRequestException();
        exception.setUsername(exceptionMessage);
        throw exception;
    }

    public static void throwMessageException(String exceptionMessage) {
        GenericRequestException exception = new GenericRequestException();
        exception.setMessage(exceptionMessage);
        throw exception;
    }

    public static void throwTokenException(String exceptionMessage) {
        GenericRequestException exception = new GenericRequestException();
        exception.setToken(exceptionMessage);
        throw exception;
    }

    public static void throwPasswordException(String exceptionMessage) {
        GenericRequestException exception = new GenericRequestException();
        exception.setPassword(exceptionMessage);
        throw exception;
    }
}
