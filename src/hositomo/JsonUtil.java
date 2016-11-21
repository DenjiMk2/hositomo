package hositomo;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
     /**
     * JSON文字列をDTOクラスへ変換する
     * @param dto
     * @param json
     * @return DTOobject
     * @throws IOException
     */
     public static <T> T parse(Class<T> dto, String json){
          ObjectMapper mapper = new ObjectMapper();
          try{
               return (T) mapper.readValue(json, dto);
          }catch(IOException e){
               return null;
          }
     }
     
     public static <T> ArrayList<T> arrayListParse(String json){
    	 ObjectMapper mapper = new ObjectMapper();
    	 try{
//    		 ArrayList<T> list = mapper.readValue(json, new TypeReference<ArrayList<T>>() {});
    		 ArrayList<T> list = mapper.readValue(json, new TypeReference<ArrayList<T>>() {});

    		 return list;
    	 } catch (IOException e) {
    		 return null;
		}
     }
     /**
     * DTOクラスのインスタンスをJSON文字列に変換する
     * @param dto
     * @return String
     * @throws JsonProcessingException
     */
     public static String convert(Object dto){
          ObjectMapper mapper = new ObjectMapper();
          try{
               String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
               return json;
          }catch(JsonProcessingException e){
               return null;
          }
     }
}