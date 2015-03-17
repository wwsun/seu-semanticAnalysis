package SqlTransformer;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/3/15.
 */
public abstract class BasicSqltransformer{
    protected String[] Params;

    public BasicSqltransformer(String[] Params){
        this.Params=Arrays.copyOf(Params,Params.length);
    }

    public abstract String[] NodesSqltransform();

    public abstract String[] EdgesSqltransform();
}
