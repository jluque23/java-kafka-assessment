package com.example.interview.service;

import com.example.interview.entity.Scan;
import com.example.interview.repository.ScanRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class KafkaConsumerService {

    @Autowired
    private ScanRepository scanRepository;


    @KafkaListener(id = "scan-listener-spring", topics = "scan_topic", groupId = "scan_group", concurrency = "1")
    public void consumeScan(ConsumerRecord<String, String> record) {
        System.out.println("Received message. Key: " + record.key() + ", Value: " + record.value());

        ObjectMapper mapper = new ObjectMapper();
        Scan scan;
        try {
            
            JsonNode node = mapper.readTree(record.value());

            scan = new Scan();
            scan.setProductId(node.get("productId").asText());
            scan.setTimestamp(LocalDateTime.parse(node.get("timestamp").asText(), DateTimeFormatter.ISO_DATE_TIME));
            scan.setType(node.get("type").asText());
            scan.setRobotId(node.get("robotId").asText());
            scan.setBarcode(node.get("barcode").asText());
            scan.setShelfLocation(node.get("shelfLocation").asText());

            scanRepository.save(scan);
        } catch (JsonMappingException e) {
            System.out.println("JsonMappingException: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException: " + e.getMessage());
        }
    }
}


