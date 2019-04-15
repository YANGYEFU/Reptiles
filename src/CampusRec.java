import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author darkf
 */
public class CampusRec {

    public static void main(String[] args) {
        getNow();
    }

    private static void getNow(){
        URL url;
        URLConnection conn;
        FileWriter fw;
        String line;
        //正则表达式
        String reg = "[\\u4e00-\\u9fa50-9\\:]+[\\ 0-9\\:]*";
        Pattern p = Pattern.compile(reg);
        //设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dt = Calendar.getInstance();
        Date date=null;
        //设置开始的日期
        String d="2017-09-01";
        try {
            date=sdf.parse(d);
            //保存数据的文件路径
            fw=new FileWriter("D:\\IDM\\2017_09-2018_12-校招信息.txt");
            //设置结束的日期
            int begin = 0;
            while (!sdf.parse("2019-01-01").equals(date)){
                url = new URL("http://job.cqupt.edu.cn/portal/home/calendar-page.html?fairDate="+sdf.format(date)+ "%2000:00");
                conn = url.openConnection();
                begin=0;

                BufferedReader bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedWriter bufw = new BufferedWriter(fw);

                bufw.write(sdf.format(date));
                bufw.newLine();
                while ((line = bufr.readLine()) != null) {
                    Matcher m = p.matcher(line);
                    //设置开始记录的行数
                    if(begin++ < 190){
                        continue;
                    }
                    if (m.find()) {
                        //过滤字符串长度低于3的行数；跳过后面的无效数据，即“时光轴”及其之后的的行数
                        if(m.group().length()<3){
                            continue;
                        }else if ("时光轴".equals(m.group())){
                            break;
                        }
                        bufw.write(m.group() + "\n");
                        bufw.flush();
                    }
                }
                //使用---作为每一天的分割
                bufw.write("----------\n");
                bufw.flush();
                dt.setTime(date);
                //日期加1天
                dt.add(Calendar.DAY_OF_YEAR,1);
                date=dt.getTime();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
