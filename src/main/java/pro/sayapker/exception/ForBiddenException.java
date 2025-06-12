package ebook.exceptions;

public class ForBiddenException extends RuntimeException {
    public ForBiddenException() {

    }
    public ForBiddenException(String message) {
        super(message);
    }
}
