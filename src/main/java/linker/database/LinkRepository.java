package linker.database;

import linker.links.LinkResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends MongoRepository<LinkResponse, String> {

    LinkResponse findByShortLink(String shortLink);
}
