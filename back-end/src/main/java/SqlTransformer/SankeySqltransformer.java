package SqlTransformer;

/**
 * Created by Administrator on 2015/3/15.
 */
public class SankeySqltransformer extends BasicSqltransformer {

    public SankeySqltransformer(String[] Params){
        super(Params);
    }

    @Override
    public String[] NodesSqltransform() {
        int loop=5;
        String[] sqls=new String[loop];
        for(int i=1;i<=loop;i++){
            String sql="select distinct P"+i+" from streams;";
            sqls[i-1]=sql;
        }
        return sqls;
    }

    @Override
    public String[] EdgesSqltransform() {
        int loop=4;
        String[] sqls=new String[loop];
        for(int i=1;i<=loop;i++){
            String start="P"+i;
            String end="P"+(i+1);
            String sql="select count(*),"+start+","+end+" from streams group by "+start+","+end;
            sqls[i-1]=sql;
        }
        return sqls;
    }
}
