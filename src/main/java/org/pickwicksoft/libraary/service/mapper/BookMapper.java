package org.pickwicksoft.libraary.service.mapper;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.pickwicksoft.bookgrabber.model.Identifier;
import org.pickwicksoft.bookgrabber.model.Publisher;
import org.pickwicksoft.libraary.domain.Author;
import org.pickwicksoft.libraary.domain.Book;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface BookMapper {
    @Named("authorsToDomainAuthors")
    static List<Author> authorsToDomainAuthors(List<org.pickwicksoft.bookgrabber.model.Author> authors) {
        return authors.stream().map(x -> new Author((x.getName()))).collect(Collectors.toList());
    }

    @Named("publishersToString")
    static String publishersToString(List<Publisher> publishers) {
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
    static Long parseISBN(Identifier identifier) {
        if (identifier.getIsbn13() != null && !identifier.getIsbn13().isEmpty()) {
            return Long.decode(identifier.getIsbn13().get(0));
        }
        return null;
    }

    @Mapping(source = "numberOfPages", target = "pages")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "authors", target = "authors", qualifiedByName = "authorsToDomainAuthors")
    @Mapping(source = "publishers", target = "publisher", qualifiedByName = "publishersToString")
    @Mapping(source = "publishDate", target = "publicationYear", qualifiedByName = "parseYearFromString")
    @Mapping(source = "identifiers", target = "isbn", qualifiedByName = "parseISBN")
    @Mapping(source = "cover", target = "cover", ignore = true)
    Book bookToDomainBook(org.pickwicksoft.bookgrabber.model.Book book);
}
