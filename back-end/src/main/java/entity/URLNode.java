package entity;

/**
 * Created by Administrator on 2015/3/16.
 */
public class URLNode extends Node {
    protected String url;
    public URLNode(int name,String url){
        super(name);
        this.url=url;
    }
}
