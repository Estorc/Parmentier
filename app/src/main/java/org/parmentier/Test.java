import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Test {

    public static void main(String[] args) throws Exception {

        JSONObject jo = new JSONObject();
        jo.put("firstName", "John");

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String prettyJson = writer.writeValueAsString(jo);
        PrintWriter pw = new PrintWriter("JSONExample.json");
        pw.write(prettyJson);
        pw.close();
        System.out.println(prettyJson);
    }
}