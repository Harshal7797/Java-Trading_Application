package ca.jrvs.apps.model.config;

public class MarketDataConfig {

    private String  url="";
    private String publicToken = "";

    public String getURl(){
        return url;
    }
    public void setUrl(String baseurl){
        this.url=baseurl;
    }
    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

}
