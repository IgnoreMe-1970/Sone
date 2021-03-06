package net.pterodactylus.sone.web.ajax

import net.pterodactylus.sone.data.Post
import net.pterodactylus.sone.data.Sone
import net.pterodactylus.sone.test.getInstance
import net.pterodactylus.sone.test.mock
import net.pterodactylus.sone.test.whenever
import net.pterodactylus.sone.utils.asOptional
import net.pterodactylus.sone.web.baseInjector
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.Test

/**
 * Unit test for [CreatePostAjaxPage].
 */
class CreatePostAjaxPageTest : JsonPageTest("createPost.ajax", pageSupplier = ::CreatePostAjaxPage) {

	@Test
	fun `missing text parameter returns error`() {
		assertThatJsonFailed("text-required")
	}

	@Test
	fun `empty text returns error`() {
		addRequestParameter("text", "")
		assertThatJsonFailed("text-required")
	}

	@Test
	fun `whitespace-only text returns error`() {
		addRequestParameter("text", "  ")
		assertThatJsonFailed("text-required")
	}

	@Test
	fun `request with valid data creates post`() {
		addRequestParameter("text", "test")
		val post = createPost()
		whenever(core.createPost(currentSone, null, "test")).thenReturn(post)
		assertThatJsonIsSuccessful()
		assertThat(json["postId"]?.asText(), equalTo("id"))
		assertThat(json["sone"]?.asText(), equalTo(currentSone.id))
		assertThat(json["recipient"], nullValue())
	}

	@Test
	fun `request with invalid recipient creates post without recipient`() {
		addRequestParameter("text", "test")
		addRequestParameter("recipient", "invalid")
		val post = createPost()
		whenever(core.createPost(currentSone, null, "test")).thenReturn(post)
		assertThatJsonIsSuccessful()
		assertThat(json["postId"]?.asText(), equalTo("id"))
		assertThat(json["sone"]?.asText(), equalTo(currentSone.id))
		assertThat(json["recipient"], nullValue())
	}

	@Test
	fun `request with valid data and recipient creates correct post`() {
		addRequestParameter("text", "test")
		addRequestParameter("recipient", "valid")
		val recipient = mock<Sone>().apply { whenever(id).thenReturn("valid") }
		addSone(recipient)
		val post = createPost("valid")
		whenever(core.createPost(currentSone, recipient, "test")).thenReturn(post)
		assertThatJsonIsSuccessful()
		assertThat(json["postId"]?.asText(), equalTo("id"))
		assertThat(json["sone"]?.asText(), equalTo(currentSone.id))
		assertThat(json["recipient"]?.asText(), equalTo("valid"))
	}

	@Test
	fun `text is filtered correctly`() {
		addRequestParameter("text", "Link http://freenet.test:8888/KSK@foo is filtered")
		addRequestHeader("Host", "freenet.test:8888")
		val post = createPost()
		whenever(core.createPost(currentSone, null, "Link KSK@foo is filtered")).thenReturn(post)
		assertThatJsonIsSuccessful()
		assertThat(json["postId"]?.asText(), equalTo("id"))
		assertThat(json["sone"]?.asText(), equalTo(currentSone.id))
		assertThat(json["recipient"], nullValue())
	}

	private fun createPost(recipientId: String? = null) =
			mock<Post>().apply {
				whenever(id).thenReturn("id")
				whenever(sone).thenReturn(currentSone)
				whenever(this.recipientId).thenReturn(recipientId.asOptional())
			}

	@Test
	fun `page can be created by dependency injection`() {
	    assertThat(baseInjector.getInstance<CreatePostAjaxPage>(), notNullValue())
	}

}
