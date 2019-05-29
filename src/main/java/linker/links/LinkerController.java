package linker.links;

import java.io.IOException;
import java.time.LocalDateTime;

import linker.database.LinkRepository;
import linker.error.LinkNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class LinkerController {

    @Autowired
    private LinkRepository repository;

    @RequestMapping(value = "/linker/links", method = GET)
    public LinkResponse getFullLink(@RequestParam(value = "short") String link) throws LinkNotFoundException {
        LinkResponse response = repository.findByShortLink(link);
        if (response == null) {
            throw new LinkNotFoundException(link);
        }
        return response;
    }

    @RequestMapping(value = "/linker/links", method = POST)
    public LinkResponse getShortenedLink(@RequestParam(value = "link") String link) throws NullPointerException, InterruptedException {
        String shortened = LinkGenerator.shortenLink(link);
        LinkResponse response = new LinkResponse(link, shortened, LocalDateTime.now());

        repository.save(response);
        return response;
    }

    @RequestMapping("/linker/redirect")
    public void redirect(HttpServletResponse response, @RequestParam(value = "short") String link) throws IOException, LinkNotFoundException {
        LinkResponse result = repository.findByShortLink(link);
        if (result == null) {
            throw new LinkNotFoundException(link);
        }
        response.sendRedirect(result.getLink());
    }
}