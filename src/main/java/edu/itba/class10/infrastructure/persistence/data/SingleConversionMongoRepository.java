package edu.itba.class10.infrastructure.persistence.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SingleConversionMongoRepository extends MongoRepository<SingleConversionDocument, String> {
}
