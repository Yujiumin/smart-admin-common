package jiumin.yu.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jiumin.yu.common.json.deserializer.LocalDateTimeDeserializer;
import jiumin.yu.common.json.serializer.LocalDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;

/**
 * @author Yujiumin
 * @date 2022/11/20
 */
public class SmartAdminObjectMapper {

    public static ObjectMapper objectMapper;

    public static ObjectMapper objectMapperForCache;

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)

                //设置数组转换方式，true:如果待转换对象的属性是数组的话, 则将单个string转换到数组形式
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)

                //设置空字符串转换为null
                .featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

                //设置属性忽略，false:忽略不认识的属性，不抛错,true:遇到无法转换的属性，抛错
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();

        TypeResolverBuilder<?> typer = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL, LaissezFaireSubTypeValidator.instance);
        typer = typer.init(JsonTypeInfo.Id.CLASS, null);
        typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
        objectMapperForCache = Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)

                //设置数组转换方式，true:如果待转换对象的属性是数组的话, 则将单个string转换到数组形式
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)

                //设置空字符串转换为null
                .featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

                //设置属性忽略，false:忽略不认识的属性，不抛错,true:遇到无法转换的属性，抛错
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .defaultTyping(typer)
                .build();
    }
}
