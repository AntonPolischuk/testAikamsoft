package program;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import criterias.CriteriaStatForThePeriod;
import criterias.CriteriaForSearch;
import dto.InterfaceDto;
import util.ConnectionDB;
import dao.CustomerDao;
import dao.StatisticDao;
import util.ExceptionHandler;
import util.JsonParser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class Program {

        public static void main(String[] args) throws IOException, SQLException {

            ExceptionHandler.createInstance(args[2]);
            InterfaceDto interfaceDto=null;

            if(args[0].equals("search")){
                CriteriaForSearch criteriaForSearch = JsonParser.parser(args[1]);
                CustomerDao customerDao = new CustomerDao(criteriaForSearch.getCriterias());
                try {
                    interfaceDto = customerDao.getSearchDto();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if(args[0].equals("stat")){
                CriteriaStatForThePeriod criteriaStatForThePeriod = JsonParser.parserForPeriod(args[1]);
                StatisticDao statisticDao = new StatisticDao(criteriaStatForThePeriod);
                interfaceDto = statisticDao.getStatisticDto();
            }
            else {
                ExceptionHandler.getInstance().createErrorToOutputFile("Не верно указан тип операции при запуске программы");
                throw new IOException();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            try {
                mapper.writeValue(new File(args[2]), interfaceDto);
            } catch (IOException e) { //TODO: обработать
                e.printStackTrace();
            }
            ConnectionDB.closeConnection();
        }


        }

