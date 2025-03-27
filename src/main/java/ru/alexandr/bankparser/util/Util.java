package ru.alexandr.bankparser.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Getter
public class Util {

     @Value("${currencies}")
     public List<String> currencies;

}
