/*
 * Sone - PostReplyImpl.java - Copyright © 2010–2013 David Roden
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

import java.util.Set;

import net.pterodactylus.sone.data.Post;
import net.pterodactylus.sone.data.PostReply;
import net.pterodactylus.sone.data.Sone;
import net.pterodactylus.sone.database.Database;

import com.google.common.base.Optional;

/**
 * Simple {@link PostReply} implementation.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultPostReply extends DefaultReply<PostReply> implements PostReply {

	/** The Post this reply refers to. */
	private final String postId;

	/**
	 * Creates a new reply.
	 *
	 * @param database
	 * 		The database
	 * @param id
	 * 		The ID of the reply
	 * @param soneId
	 * 		The ID of the Sone of the reply
	 * @param time
	 * 		The time of the reply
	 * @param text
	 * 		The text of the reply
	 * @param postId
	 */
	public DefaultPostReply(Database database, String id, String soneId, long time, String text, String postId) {
		super(database, id, soneId, time, text);
		this.postId = postId;
	}

	//
	// ACCESSORS
	//

	@Override
	public boolean isKnown() {
		return database.isPostReplyKnown(this);
	}

	@Override
	public String getPostId() {
		return postId;
	}

	@Override
	public Optional<Post> getPost() {
		return database.getPost(postId);
	}

	@Override
	public void like(Sone localSone) {
		database.likePostReply(this, localSone);
	}

	@Override
	public void unlike(Sone localSone) {
		database.unlikePostReply(this, localSone);
	}

	@Override
	public boolean isLiked(Sone sone) {
		return database.isLiked(this, sone);
	}

	@Override
	public Set<Sone> getLikes() {
		return database.getLikes(this);
	}

	@Override
	public Modifier<PostReply> modify() {
		return new Modifier<PostReply>() {
			private boolean known = isKnown();

			@Override
			public Modifier<PostReply> setKnown() {
				known = true;
				return this;
			}

			@Override
			public PostReply update(Optional<ReplyUpdated<PostReply>> replyUpdated) {
				if (known) {
					database.setPostReplyKnown(DefaultPostReply.this);
				}
				if (replyUpdated.isPresent()) {
					replyUpdated.get().replyUpdated(DefaultPostReply.this);
				}
				return DefaultPostReply.this;
			}
		};
	}

}
