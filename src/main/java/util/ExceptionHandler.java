package util;


import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDto;

import java.io.File;
import java.io.IOException;

public class ExceptionHandler {

    private  String outputFileName;

    private static ExceptionHandler exceptionHandler;

    private ExceptionHandler(String outputFileName){
        this.outputFileName=outputFileName;
    }

    public static void createInstance (String outputFileName) {
       if(exceptionHandler==null) exceptionHandler=new ExceptionHandler(outputFileName);
    }

    public static ExceptionHandler getInstance(){
        return exceptionHandler;
    }

    public void createErrorToOutputFile(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(outputFileName), new ErrorDto("error",message));
    }

}
