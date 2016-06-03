package com.mastercard.masterpass.test.merchant.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Helper methods for dealing with XMLs
 */
public class XMLUtil {
    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(XMLUtil.class);

    /**
     * Converts object to XML representation
     *
     * @param clazz  object's class
     * @param object object to serialize
     * @return XML with object data
     */
    public static String ObjectToXML(Class<?> clazz, Object object) {
        JAXBContext context;
        String xmlRepresentation = null;

        try {
            context = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.fragment", Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();

            jaxbMarshaller.marshal(castObject(clazz, object), stringWriter);
            xmlRepresentation = stringWriter.toString();
        } catch (Exception e) {
            logger.error("convert object to xml error: " + e.getMessage());
            e.printStackTrace();
        }
        return xmlRepresentation;
    }

    /**
     * Converts String xml to model object. Unmarshalling xml
     * @param clazz result object's class
     * @param xml String with xml
     * @param <T> type
     * @return object of type T or null
     */
    public static <T> T XMLToObject(Class<T> clazz, String xml){
        JAXBContext context;

        try{
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            StreamSource streamSource = new StreamSource(stringReader);
            JAXBElement<T> jaxbElement =  unmarshaller.unmarshal(streamSource, clazz);
            return jaxbElement.getValue();
        }catch (Exception e){
            logger.error("convert xml to object error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T castObject(Class<T> clazz, Object object) {
        return (T) clazz.cast(object);
    }

}
