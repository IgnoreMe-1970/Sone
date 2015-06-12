/*
 * Sone - PostReplyStore.java - Copyright © 2013 David Roden
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

package net.pterodactylus.sone.database;

import java.util.Collection;

import net.pterodactylus.sone.data.PostReply;
import net.pterodactylus.sone.data.Sone;

/**
 * Defines a store for {@link PostReply post replies}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface PostReplyStore {

	/**
	 * Stores the given post reply.
	 *
	 * @param postReply
	 *            The post reply
	 */
	public void storePostReply(PostReply postReply);

	/**
	 * Removes the given post reply from this store.
	 *
	 * @param postReply
	 *            The post reply to remove
	 */
	public void removePostReply(PostReply postReply);

}
