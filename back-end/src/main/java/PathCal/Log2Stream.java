package PathCal;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import me.wwsun.dao.DictDAO;
import me.wwsun.dao.LogDAO;
import me.wwsun.dao.StreamDAO;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/3/14.
 */
public class Log2Stream {
    private String mongoURI;
    private String db;
    final MongoClient mongoClient;
    public final static int url=0;
    public final static int go=1;
    public final static int url_level=2;
    public final static int go_level=3;
    final int struc_size=4;


    public Log2Stream(String params[]) throws UnknownHostException {
        mongoURI=params[0];
        db=params[1];
        mongoClient = new MongoClient(new MongoClientURI(mongoURI));
    }

    public List<DBObject> getSequenceBySession(String session) {
        final DB siteDatabase = mongoClient.getDB(db);
        LogDAO logDAO = new LogDAO(siteDatabase);
        return logDAO.getSessionSequenceById(session);
    }

    public Integer getIndxByUrl(String url) {
        final DB siteDatabase = mongoClient.getDB(db);
        DictDAO dictDAO = new DictDAO(siteDatabase);
        return dictDAO.getUrlIndex(url);
    }

    public List<ArrayList<String[]>> getStreamsOfSequence(List<DBObject> sequence){
        List<ArrayList<String[]>> streams=new ArrayList<ArrayList<String[]>>();

        for(DBObject ob:sequence) {
            String[] point=new String[struc_size];
            point[url]=ob.get("referer").toString();
            point[go]=ob.get("request").toString();
            point[go_level]=ob.get("level3").toString();
            if(streams.size()==0){
                List<String[]> stream=new ArrayList<String[]>();
                point[url_level]="000";
                stream.add(point);
                streams.add((ArrayList)stream);
            }
            else{
                int k;
                for(k=streams.size()-1;k>=0;k--){
                    List<String[]> stream=streams.get(k);
                    if(stream.get(stream.size()-1)[go].equals(ob.get("referer"))){
                        point[url_level]=stream.get(stream.size() - 1)[go_level];
                        stream.add(point);
                        break;
                    }
                    else{
                        int index=-1;
                        for(int j=stream.size()-2;j>=0;j--){
                            if(stream.get(j)[go].equals(ob.get("referer"))){
                                index=j;
                                break;
                            }
                        }
                        if(index!=-1){
                            List<String[]> newstream=new ArrayList<String[]>();
                            for(int j=0;j<=index;j++){
                                newstream.add(stream.get(j));
                            }
                            point[url_level]=stream.get(index)[go_level];
                            newstream.add(point);
                            streams.add((ArrayList)newstream);
                            break;
                        }
                    }
                }
                if(k==-1){
                    List<String[]> newstream=new ArrayList<String[]>();
                    point[url_level]="000";
                    newstream.add(point);
                    streams.add((ArrayList)newstream);
                }
            }
        }
        return streams;
    }

    public static void main(String args[]) throws UnknownHostException {
        String params[]={
                "mongodb://223.3.75.101:27017",
                "jiaodian",
        };
        Log2Stream log = new Log2Stream(params);
        String session = "zcuMjE2LjMuNjMyMDE0MDgxMDAyMzU0ODU1MjI5MDU4ODAM";
        List<DBObject> sequence = log.getSequenceBySession(session);
        List<ArrayList<String[]>> streams=log.getStreamsOfSequence(sequence);

        for(List<String[]> stream:streams){
            for(String[] point:stream) {
                System.out.print(log.getIndxByUrl(point[log.url])+"->");
            }
            System.out.println();
        }

        String[] empty={"nul","nul","nul","nul"};
        for(List<String[]> stream:streams){
            int size=stream.size();
            if(size<10){
                for(int i=size;i<10;i++)
                    stream.add(empty);
            }
            else if(size>10){
                for(int i=size-1;i>=10;i--)
                    stream.remove(i);
            }
        }

//        System.out.println("======================");
//        for(List<String[]> stream:streams){
//            for(String[] point:stream) {
//                System.out.print(log.getIndxByUrl(point[log.url])+"->");
//            }
//            System.out.println();
//        }

        StreamDAO strDao = new StreamDAO("jdbc:mysql://localhost:3306","log_stream");
        strDao.insertStreams(streams,session);
    }
}
