package requestPath;

public enum SwapiRequestPath {
    PEOPLE("people");

    String path;

    SwapiRequestPath(String path) {
        this.path = path;
    }

    public String getValue(){
        return path;
    }
}
