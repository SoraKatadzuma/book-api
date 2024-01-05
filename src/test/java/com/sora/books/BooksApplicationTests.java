package com.sora.books;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sora.books.controller.BookController;
import com.sora.books.transfer.AuthorDTO;
import com.sora.books.transfer.BookDTO;
import com.sora.books.transfer.PublicationDTO;
import com.sora.books.transfer.PublisherDTO;

@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BooksApplicationTests {
	@Autowired
	MockMvc mvc;

	@Autowired
	BookController bookController;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void test_ContextLoads() throws Exception {
		assertNotNull(bookController);
	}

	@Test
	void test_WithValidInput_CreateBook_IsOk() throws Exception {
		var publisher = PublisherDTO.builder()
			.name("Publisher INC.")
			.locale("US")
			.build();
			
		var author = AuthorDTO.builder()
			.fullName("John Christman")
            .dateOfBirth(LocalDate.of(1998, 1, 16))
            .dateOfDeath(LocalDate.of(2069, 5, 25))
            .pseudonyms(new HashSet<>(Arrays.asList("SoraKatadzuma", "VellaSpring")))
            .build();

		var publication = PublicationDTO.builder()
			.publisher(publisher)
			.edition(1l)
			.copyright(LocalDate.of(2030, 8, 12))
			.build();

		var bookDto = BookDTO.builder()
			.name("Reverie Tides")
			.authors(new HashSet<>(Arrays.asList(author)))
			.publications(new HashSet<>(Arrays.asList(publication)))
			.build();

		var content = objectMapper.writeValueAsString(bookDto);
		var result  = mvc.perform(
			MockMvcRequestBuilders
				.post("/api/v1/book")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isCreated())
		 .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		 .andReturn()
		 .getResponse()
		 .getContentAsString();

		var book = objectMapper.readValue(result, BookDTO.class);
		assertEquals(2l, book.getId());
		assertEquals("Reverie Tides", book.getName());
		assertNotNull(book.getAdded());
		assertNull(book.getDeleted());

		var authors = book.getAuthors().toArray(AuthorDTO[]::new);
		assertEquals(2l, authors[0].getId());
		assertEquals("John Christman", authors[0].getFullName());
		assertEquals("1998-01-16", authors[0].getDateOfBirth().toString());
		assertEquals("2069-05-25", authors[0].getDateOfDeath().toString());

		var pseudonyms = new HashSet<>(Arrays.asList("SoraKatadzuma", "VellaSpring"));
		assertEquals(pseudonyms, authors[0].getPseudonyms());

		var publications = book.getPublications().toArray(PublicationDTO[]::new);
		assertEquals(3l, publications[0].getPublisher().getId());
		assertEquals("Publisher INC.", publications[0].getPublisher().getName());
		assertEquals("US", publications[0].getPublisher().getLocale());
		assertEquals(1l, publications[0].getEdition());
		assertEquals("2030-08-12", publications[0].getCopyright().toString());
	}

	@Test
	void test_WithNullInput_CreateBook_BadRequest() throws Exception {
		mvc.perform(
			MockMvcRequestBuilders
				.post("/api/v1/book")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void test_ReadBook_ReturnsBook_IsOk() throws Exception {
		var result = mvc.perform(
			MockMvcRequestBuilders
				.get("/api/v1/book/0")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		 .andReturn()
		 .getResponse()
		 .getContentAsString();

		var book = objectMapper.readValue(result, BookDTO.class);
		assertEquals(0l, book.getId());
		assertEquals("Moby Dick", book.getName());
		assertNotNull(book.getAdded());
		assertNull(book.getDeleted());

		var authors = book.getAuthors().toArray(AuthorDTO[]::new);
		assertEquals(0l, authors[0].getId());
		assertEquals("Herman Melville", authors[0].getFullName());
		assertEquals("1819-08-01", authors[0].getDateOfBirth().toString());
		assertEquals("1891-09-28", authors[0].getDateOfDeath().toString());

		var pseudonyms = new HashSet<>(Arrays.asList("L.A.V.", "Salvator R. Tarnmoor"));
		assertEquals(pseudonyms, authors[0].getPseudonyms());

		var publications = book.getPublications().toArray(PublicationDTO[]::new);
		assertEquals(0l, publications[0].getPublisher().getId());
		assertEquals("Richard Bentley", publications[0].getPublisher().getName());
		assertEquals("UK", publications[0].getPublisher().getLocale());
		assertEquals(1l, publications[0].getEdition());
		assertEquals("1851-10-18", publications[0].getCopyright().toString());

		assertEquals(1l, publications[1].getPublisher().getId());
		assertEquals("Harper & Brothers", publications[1].getPublisher().getName());
		assertEquals("US", publications[1].getPublisher().getLocale());
		assertEquals(1l, publications[1].getEdition());
		assertEquals("1851-11-14", publications[1].getCopyright().toString());
	}

	@Test
	void test_UpdatedBookName() throws Exception {
		var bookInformation = BookDTO.builder()
			.name("Sailor")
			.build();

		var content = objectMapper.writeValueAsString(bookInformation);
		var result  = mvc.perform(
			MockMvcRequestBuilders
				.put("/api/v1/book/0")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		 .andReturn()
		 .getResponse()
		 .getContentAsString();

		// Validate whole book structure, with all the data of the normal book,
		// but with the name changed to "Sailor".
		var book = objectMapper.readValue(result, BookDTO.class);
		assertEquals(0l, book.getId());
		assertEquals("Sailor", book.getName()); // MOST IMPORTANTLY
		assertNotNull(book.getAdded());
		assertNull(book.getDeleted());

		var authors = book.getAuthors().toArray(AuthorDTO[]::new);
		assertEquals(0l, authors[0].getId());
		assertEquals("Herman Melville", authors[0].getFullName());
		assertEquals("1819-08-01", authors[0].getDateOfBirth().toString());
		assertEquals("1891-09-28", authors[0].getDateOfDeath().toString());

		var pseudonyms = new HashSet<>(Arrays.asList("L.A.V.", "Salvator R. Tarnmoor"));
		assertEquals(pseudonyms, authors[0].getPseudonyms());

		var publications = book.getPublications().toArray(PublicationDTO[]::new);
		assertEquals(0l, publications[0].getPublisher().getId());
		assertEquals("Richard Bentley", publications[0].getPublisher().getName());
		assertEquals("UK", publications[0].getPublisher().getLocale());
		assertEquals(1l, publications[0].getEdition());
		assertEquals("1851-10-18", publications[0].getCopyright().toString());

		assertEquals(1l, publications[1].getPublisher().getId());
		assertEquals("Harper & Brothers", publications[1].getPublisher().getName());
		assertEquals("US", publications[1].getPublisher().getLocale());
		assertEquals(1l, publications[1].getEdition());
		assertEquals("1851-11-14", publications[1].getCopyright().toString());
	}

	@Test
	void test_UpdateBookAuthor() throws Exception {
		var author = AuthorDTO.builder()
			.fullName("John Christman")
            .dateOfBirth(LocalDate.of(1998, 1, 16))
            .dateOfDeath(LocalDate.of(2069, 5, 25))
            .pseudonyms(new HashSet<>(Arrays.asList("SoraKatadzuma", "VellaSpring")))
            .build();

		var bookDto = BookDTO.builder()
			.authors(new HashSet<>(Arrays.asList(author)))
			.build();

		var content = objectMapper.writeValueAsString(bookDto);
		var result  = mvc.perform(
			MockMvcRequestBuilders
				.put("/api/v1/book/0")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		 .andReturn()
		 .getResponse()
		 .getContentAsString();

		var book = objectMapper.readValue(result, BookDTO.class);
		assertEquals(0l, book.getId());
		assertEquals("Moby Dick", book.getName());
		assertNotNull(book.getAdded());
		assertNull(book.getDeleted());

		var authors = book.getAuthors().toArray(AuthorDTO[]::new);
		assertEquals(3l, authors[0].getId());
		assertEquals("John Christman", authors[0].getFullName());
		assertEquals("1998-01-16", authors[0].getDateOfBirth().toString());
		assertEquals("2069-05-25", authors[0].getDateOfDeath().toString());

		var pseudonyms = new HashSet<>(Arrays.asList("SoraKatadzuma", "VellaSpring"));
		assertEquals(pseudonyms, authors[0].getPseudonyms());

		var publications = book.getPublications().toArray(PublicationDTO[]::new);
		assertEquals(0l, publications[0].getPublisher().getId());
		assertEquals("Richard Bentley", publications[0].getPublisher().getName());
		assertEquals("UK", publications[0].getPublisher().getLocale());
		assertEquals(1l, publications[0].getEdition());
		assertEquals("1851-10-18", publications[0].getCopyright().toString());

		assertEquals(1l, publications[1].getPublisher().getId());
		assertEquals("Harper & Brothers", publications[1].getPublisher().getName());
		assertEquals("US", publications[1].getPublisher().getLocale());
		assertEquals(1l, publications[1].getEdition());
		assertEquals("1851-11-14", publications[1].getCopyright().toString());
	}

	@Test
	void test_UpdateBookPublication() throws Exception {
		var publisher = PublisherDTO.builder()
			.id(1l)
			.name("Harper & Brothers")
			.locale("US")
			.build();

		var publication = PublicationDTO.builder()
			.publisher(publisher)
			.edition(1l)
			.copyright(LocalDate.of(1851, 11, 14))
			.build();

		var bookDto = BookDTO.builder()
			.publications(new HashSet<>(Arrays.asList(publication)))
			.build();

		var content = objectMapper.writeValueAsString(bookDto);
		var result  = mvc.perform(
			MockMvcRequestBuilders
				.put("/api/v1/book/0")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		 .andReturn()
		 .getResponse()
		 .getContentAsString();

		var book = objectMapper.readValue(result, BookDTO.class);
		assertEquals(0l, book.getId());
		assertEquals("Moby Dick", book.getName());
		assertNotNull(book.getAdded());
		assertNull(book.getDeleted());

		var authors = book.getAuthors().toArray(AuthorDTO[]::new);
		assertEquals(0l, authors[0].getId());
		assertEquals("Herman Melville", authors[0].getFullName());
		assertEquals("1819-08-01", authors[0].getDateOfBirth().toString());
		assertEquals("1891-09-28", authors[0].getDateOfDeath().toString());

		var pseudonyms = new HashSet<>(Arrays.asList("L.A.V.", "Salvator R. Tarnmoor"));
		assertEquals(pseudonyms, authors[0].getPseudonyms());

		var publications = book.getPublications().toArray(PublicationDTO[]::new);
		assertEquals(1l, publications[0].getPublisher().getId());
		assertEquals("Harper & Brothers", publications[0].getPublisher().getName());
		assertEquals("US", publications[0].getPublisher().getLocale());
		assertEquals(1l, publications[0].getEdition());
		assertEquals("1851-11-14", publications[0].getCopyright().toString());
	}

	@Test
	void test_WithNullInput_UpdateBook_IsBadRequest() throws Exception {
		mvc.perform(
			MockMvcRequestBuilders
				.put("/api/v1/book/0")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void test_DeleteBook() throws Exception {
		mvc.perform(
			MockMvcRequestBuilders
				.delete("/api/v1/book/0")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isNoContent());

		mvc.perform(
			MockMvcRequestBuilders
				.get("/api/v1/book/0")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
