/*
 * Sone - ImageBuilderImpl.java - Copyright © 2013 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.sone.data.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import net.pterodactylus.sone.data.Image;
import net.pterodactylus.sone.data.Sone;
import net.pterodactylus.sone.database.ImageBuilder;

/**
 * {@link ImageBuilder} implementation that creates {@link DefaultImage} objects.
 *
 * @author <a href="mailto:d.roden@xplosion.de">David Roden</a>
 */
public class DefaultImageBuilder extends AbstractImageBuilder {

	protected final Sone sone;
	protected final DefaultAlbum album;

	public DefaultImageBuilder(Sone sone, DefaultAlbum album) {
		this.sone = checkNotNull(sone, "sone must not be null");
		this.album = checkNotNull(album, "album must not be null");
	}

	@Override
	public Image build() throws IllegalStateException {
		validate();
		return new DefaultImage(getId(), sone, album, key, getCreationTime(), width, height);
	}

}
