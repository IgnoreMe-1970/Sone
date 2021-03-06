package net.pterodactylus.sone.web.pages

import net.pterodactylus.sone.data.*
import net.pterodactylus.sone.data.impl.*
import net.pterodactylus.sone.test.getInstance
import net.pterodactylus.sone.test.mock
import net.pterodactylus.sone.test.whenever
import net.pterodactylus.sone.web.*
import net.pterodactylus.sone.web.page.*
import net.pterodactylus.util.web.Method.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.*
import org.mockito.Mockito.*
import org.mockito.Mockito.eq

/**
 * Unit test for [UploadImagePage].
 */
class UploadImagePageTest : WebPageTest(::UploadImagePage) {

	private val parentAlbum = AlbumImpl(currentSone, "parent-id")

	@Test
	fun `page returns correct path`() {
		assertThat(page.path, equalTo("uploadImage.html"))
	}

	@Test
	fun `page requires login`() {
		assertThat(page.requiresLogin(), equalTo(true))
	}

	@Test
	fun `page returns correct title`() {
		addTranslation("Page.UploadImage.Title", "upload image page title")
		assertThat(page.getPageTitle(soneRequest), equalTo("upload image page title"))
	}

	@Test
	fun `get request does not redirect or upload anything`() {
		verifyNoRedirect {
			verify(core, never()).createTemporaryImage(any(), any())
			verify(core, never()).createImage(any(), any(), any())
		}
	}

	@Test
	fun `post request without parent results in no permission error page`() {
		setMethod(POST)
		verifyRedirect("noPermission.html")
	}

	@Test
	fun `post request with parent that is not the current sone results in no permission error page`() {
		setMethod(POST)
		val remoteAlbum = AlbumImpl(mock(), "parent-id")
		addAlbum("parent-id", remoteAlbum)
		addHttpRequestPart("parent", "parent-id")
		verifyRedirect("noPermission.html")
	}

	@Test
	fun `post request with empty name redirects to error page`() {
		setMethod(POST)
		addAlbum("parent-id", parentAlbum)
		addHttpRequestPart("parent", "parent-id")
		addHttpRequestPart("title", " ")
		verifyRedirect("emptyImageTitle.html")
	}

	@Test
	fun `uploading an invalid image results in no redirect and message set in template context`() {
		setMethod(POST)
		addAlbum("parent-id", parentAlbum)
		addHttpRequestPart("parent", "parent-id")
		addHttpRequestPart("title", "title")
		addUploadedFile("image", "image.png", "image/png", "upload-image-invalid-image.png")
		addTranslation("Page.UploadImage.Error.InvalidImage", "upload error - invalid image")
		verifyNoRedirect {
			verify(core, never()).createTemporaryImage(any(), any())
			assertThat(templateContext["messages"] as String, equalTo("upload error - invalid image"))
		}
	}

	@Test
	fun `uploading a valid image uploads image and redirects to album browser`() {
		setMethod(POST)
		addAlbum("parent-id", parentAlbum)
		addHttpRequestPart("parent", "parent-id")
		addHttpRequestPart("title", "Title")
		addHttpRequestPart("description", "Description @ http://localhost:8888/KSK@foo")
		addHttpRequestHeader("Host", "localhost:8888")
		addUploadedFile("image", "upload-image-value-image.png", "image/png", "upload-image-value-image.png")
		val temporaryImage = TemporaryImage("temp-image")
		val image = ImageImpl()
		whenever(core.createTemporaryImage(eq("image/png"), any())).thenReturn(temporaryImage)
		whenever(core.createImage(currentSone, parentAlbum, temporaryImage)).thenReturn(image)
		verifyRedirect("imageBrowser.html?album=parent-id") {
			assertThat(image.width, equalTo(2))
			assertThat(image.height, equalTo(1))
			assertThat(image.title, equalTo("Title"))
			assertThat(image.description, equalTo("Description @ KSK@foo"))
		}
	}

	@Test
	fun `page can be created by dependency injection`() {
		assertThat(baseInjector.getInstance<UploadImagePage>(), notNullValue())
	}

	@Test
	fun `page is annotated with correct template path`() {
		assertThat(page.templatePath, equalTo("/templates/invalid.html"))
	}

}
