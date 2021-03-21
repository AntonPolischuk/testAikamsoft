package util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import criterias.CriteriaStatForThePeriod;
import criterias.CriteriaForSearch;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonParser {

    public static CriteriaForSearch parser(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
           return mapper.readValue(new File(path), CriteriaForSearch.class);
        } catch (FileNotFoundException e) {
            ExceptionHandler.getInstance().createErrorToOutputFile("Нет такого файла");
            throw e;
        }catch (InvalidFormatException e){
            ExceptionHandler.getInstance().createErrorToOutputFile("Не корректный формат данных для поиска");
            throw e;
        } catch (JsonMappingException | JsonParseException e){
            ExceptionHandler.getInstance().createErrorToOutputFile("Не корректная структура файла, файл не может быть прочитан");
            throw e;
        }

    }

    public static CriteriaStatForThePeriod parserForPeriod(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try {
           return mapper.readValue(new File(path), CriteriaStatForThePeriod.class);
        } catch (FileNotFoundException e) {
            ExceptionHandler.getInstance().createErrorToOutputFile("Нет такого файла");
            throw e;
        }catch (InvalidFormatException e){
            ExceptionHandler.getInstance().createErrorToOutputFile("Не правильный формат даты");
            throw e;
        }catch (JsonMappingException | JsonParseException e){
            ExceptionHandler.getInstance().createErrorToOutputFile("Не корректная структура файла, файл не может быть прочитан");
            throw e;
        }
    }

}
