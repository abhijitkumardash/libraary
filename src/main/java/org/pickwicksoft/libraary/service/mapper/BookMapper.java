package org.pickwicksoft.libraary.service.mapper;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.pickwicksoft.bookgrabber.model.Cover;
import org.pickwicksoft.bookgrabber.model.Identifier;
import org.pickwicksoft.bookgrabber.model.Publisher;
import org.pickwicksoft.libraary.domain.Author;
import org.pickwicksoft.libraary.domain.Book;
import org.springframework.stereotype.Service;

@Service
@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Named("authorsToString")
    static List<Author> authorsToString(List<org.pickwicksoft.bookgrabber.model.Author> authors) {
        return authors
            .stream()
            .map(x -> {
                Author author = new Author();
                author.setName(x.getName());
                return author;
            })
            .collect(Collectors.toList());
    }

    @Named("publishersToString")
    static String publishersToStrings(List<Publisher> publishers) {
        if (!publishers.isEmpty()) {
            return publishers.get(0).getName();
        }
        return "";
    }

    @Named("parseYearFromString")
    static Integer parseYearFromString(String date) {
        Pattern yearPattern = Pattern.compile("\\d\\d\\d\\d", Pattern.MULTILINE);
        var matcher = yearPattern.matcher(date);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(0));
        }
        return null;
    }

    @Named("parseISBN")
    static String parseISBN(Identifier identifier) {
        if (identifier.getIsbn13() != null && !identifier.getIsbn13().isEmpty()) {
            return identifier.getIsbn13().get(0);
        }
        return "";
    }

    @Named("parseCover")
    static byte[] parseCover(Cover cover) {
        return null;
    }

    @Mapping(source = "numberOfPages", target = "pages")
    @Mapping(source = "description", target = "subject")
    //@Mapping(source = "authors", target = "authors", qualifiedByName = "authorsToString")
    @Mapping(source = "publishers", target = "publisher", qualifiedByName = "publishersToString")
    @Mapping(source = "publishDate", target = "publicationYear", qualifiedByName = "parseYearFromString")
    @Mapping(source = "identifiers", target = "isbn", qualifiedByName = "parseISBN")
    @Mapping(source = "cover", target = "cover", qualifiedByName = "parseCover")
    Book bookToDomainBook(org.pickwicksoft.bookgrabber.model.Book book);
}
