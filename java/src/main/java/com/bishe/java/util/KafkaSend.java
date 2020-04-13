package com.bishe.java.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @ClassName： KafkaSend
 * @description:
 * @author: 席似诚
 * @create: 2020-01-21 15:35
 **/

public class KafkaSend {
    @Autowired
    KafkaTemplate kafkaTemplate;

}
