package org.springframework.site.indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.site.domain.projects.Project;
import org.springframework.site.domain.projects.SupportedVersions;
import org.springframework.site.indexer.mapper.ApiDocumentMapper;
import org.springframework.site.search.SearchEntry;

import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ApiDocumentProcessorTests {

	private SupportedVersions versions = SupportedVersions.build(Arrays.asList("3.2.3.RELEASE", "3.1.4.RELEASE", "4.0.0.M1"));

	private Project project = new Project("spring", "Spring",  //
			"https://github.com/SpringSource/spring-framework", //
			"http://static.springsource.org/spring/docs/{version}/reference/", //
			"http://static.springsource.org/spring/docs/{version}/api/", //
			versions);

	private ApiDocumentMapper apiDocumentMapper = new ApiDocumentMapper(project, versions.getCurrent());

	@Test
	public void mapDocumentWithValidClassMarkers() throws Exception {
		InputStream html = new ClassPathResource("/apiDocument.html", getClass()).getInputStream();
		Document document = Jsoup.parse(html, "UTF-8", "http://example.com/docs");

		SearchEntry searchEntry = apiDocumentMapper.map(document);
		assertThat(searchEntry.getRawContent(), equalTo("SomeClass"));
	}

	@Test
	public void mapDocumentWithInvalidClassMarkers() throws Exception {
		InputStream html = new ClassPathResource("/invalidApiDocument.html", getClass()).getInputStream();
		Document document = Jsoup.parse(html, "UTF-8", "http://example.com/docs");

		SearchEntry searchEntry = apiDocumentMapper.map(document);
		assertThat(searchEntry.getRawContent(), equalTo(document.text()));
	}
}
