package net.pterodactylus.sone.web.page

import com.google.inject.*
import freenet.client.*
import net.pterodactylus.sone.test.*
import net.pterodactylus.util.web.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.*

private val highLevelSimpleClient = mock<HighLevelSimpleClient>()
private const val pathPrefix = "/some/prefix/"

class PageToadletFactoryTest {

	private val pageToadletFactory = PageToadletFactory(highLevelSimpleClient, pathPrefix)

	@Test
	fun `page toadlet without menu name is created without menu name`() {
		val page = mock<Page<FreenetRequest>>()
		val pageToadlet = pageToadletFactory.createPageToadlet(page)
		assertThat(pageToadlet.menuName, nullValue())
	}

	@Test
	fun `page toadlet with menu name is created with menu name`() {
		val page = mock<Page<FreenetRequest>>()
		val pageToadlet = pageToadletFactory.createPageToadlet(page, "testName")
		assertThat(pageToadlet.menuName, equalTo("testName"))
	}

	@Test
	fun `path prefix is handed down correctly`() {
		val page = mock<Page<FreenetRequest>>().apply {
			whenever(path).thenReturn("path")
		}
		val pageToadlet = pageToadletFactory.createPageToadlet(page)
		assertThat(pageToadlet.path(), equalTo("/some/prefix/path"))
	}

	@Test
	fun `menu name is added from annotation when no menu name is given`() {
		val page = TestPageWithMenuName()
		val pageToadlet = pageToadletFactory.createPageToadlet(page)
		assertThat(pageToadlet.menuName, equalTo("testName"))
	}

	@Test
	fun `menu name from annotation is ignored when menu name is given`() {
		val page = TestPageWithMenuName()
		val pageToadlet = pageToadletFactory.createPageToadlet(page, "foo")
		assertThat(pageToadlet.menuName, equalTo("foo"))
	}

	@Test
	fun `page toadlet factory can be created by guice`() {
		val injector = Guice.createInjector(
				HighLevelSimpleClient::class.isProvidedBy(highLevelSimpleClient),
				String::class.withNameIsProvidedBy("/Sone/", "toadletPathPrefix")
		)
	    assertThat(injector.getInstance<PageToadletFactory>(), notNullValue())
	}

}

@MenuName("testName")
private class TestPageWithMenuName : Page<FreenetRequest> {

	override fun getPath() = ""
	override fun isPrefixPage() = false
	override fun handleRequest(request: FreenetRequest, response: Response) = response

}
