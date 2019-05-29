package linker.error;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String link) {
        super(String.format("Short link %s is not found (it might have expired)", link));
    }
}
