package com.shengfei.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateConverter implements Converter<String, Date> {

    Logger logger = LoggerFactory.getLogger(DateConverter.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Date convert(String source) {
        if (source != null && source.length() > 0) {
            try {
                return sdf.parse(source);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}