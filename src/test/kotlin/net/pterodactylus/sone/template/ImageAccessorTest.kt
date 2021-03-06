package net.pterodactylus.sone.template

import net.pterodactylus.sone.data.impl.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Test

/**
 * Unit test for [ImageAccessor].
 */
class ImageAccessorTest {

	private val accessor = ImageAccessor()

	@Test
	fun `accessor returns next image for first image`() {
		assertThat(accessor.get(null, images[0], "next"), equalTo<Any>(images[1]))
	}

	@Test
	fun `accessor returns null for next image of second image`() {
		assertThat(accessor.get(null, images[1], "next"), nullValue())
	}

	@Test
	fun `accessor returns previous image for second image`() {
		assertThat(accessor.get(null, images[1], "previous"), equalTo<Any>(images[0]))
	}

	@Test
	fun `accessor returns null for previous image of first image`() {
		assertThat(accessor.get(null, images[0], "previous"), nullValue())
	}

	@Test
	fun `accessor uses reflection accessor for all other members`() {
		assertThat(accessor.get(null, images[0], "hashCode"), equalTo<Any>(images[0].hashCode()))
	}

}

private val sone = IdOnlySone("sone")
private val album = AlbumImpl(sone)
private val images = listOf(ImageImpl().modify().setSone(sone).update(), ImageImpl().modify().setSone(sone).update()).onEach(album::addImage)
