package com.tiamo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;


/**
 * 定义redis序列化操作类，可以序列化所有类的对象，当然，该类必须实现RedisSerializer接口
 *
 */
public class RedisObjectSerializer implements RedisSerializer {

  /**
   * 序列化转换器， 对象转字节数组
   */
  private Converter<Object, byte[]> serializingConverter = new SerializingConverter();
  /**
   * 反序列化转换器, 字节数组转对象
   */
  private Converter<byte[], Object>  deserializingConverter = new DeserializingConverter();

  private final byte[] EMPTY_BYTE_ARRAY = new byte[0]; // 做一个空数组，非null

  /**
   * 做序列化处理
   * @param object
   * @return
   * @throws org.springframework.data.redis.serializer.SerializationException
   */
  @Override
  public byte[] serialize(Object object) throws SerializationException {
    if (object == null) {
      return EMPTY_BYTE_ARRAY;
    }
    return this.serializingConverter.convert(object);
  }

  /**
   * 反序列化处理
   * @param data
   * @return
   * @throws org.springframework.data.redis.serializer.SerializationException
   */
  @Override
  public Object deserialize(byte[] data) throws SerializationException {
    if (data == null || data.length == 0) {
      return null;
    }
    return this.deserializingConverter.convert(data);
  }

}
