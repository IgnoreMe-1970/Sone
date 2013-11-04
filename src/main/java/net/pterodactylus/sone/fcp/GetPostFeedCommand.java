/*
 * Sone - GetPostFeedCommand.java - Copyright © 2011–2013 David Roden
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

package net.pterodactylus.sone.fcp;

import static com.google.common.base.Optional.presentInstances;
import static com.google.common.collect.FluentIterable.from;
import static net.pterodactylus.sone.data.Sone.TO_POSTS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.pterodactylus.sone.core.Core;
import net.pterodactylus.sone.data.Post;
import net.pterodactylus.sone.data.Sone;
import net.pterodactylus.sone.freenet.fcp.FcpException;

import freenet.support.SimpleFieldSet;
import freenet.support.api.Bucket;

/**
 * Implementation of an FCP interface for other clients or plugins to
 * communicate with Sone.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class GetPostFeedCommand extends AbstractSoneCommand {

	/**
	 * Creates a new “GetPostFeed” command.
	 *
	 * @param core
	 *            The core
	 */
	public GetPostFeedCommand(Core core) {
		super(core);
	}

	@Override
	public Response execute(SimpleFieldSet parameters, Bucket data, AccessType accessType) throws FcpException {
		Sone sone = getMandatoryLocalSone(parameters, "Sone");
		int startPost = getInt(parameters, "StartPost", 0);
		int maxPosts = getInt(parameters, "MaxPosts", -1);

		List<Post> sortedPosts = from(collectAllPostsForSone(sone)).filter(Post.FUTURE_POSTS_FILTER).toSortedList(Post.TIME_COMPARATOR);

		if (sortedPosts.size() < startPost) {
			return new Response("PostFeed", encodePosts(Collections.<Post>emptyList(), "Posts."));
		}

		return new Response("PostFeed", encodePostsWithReplies(sortedPosts.subList(startPost, (maxPosts == -1) ? sortedPosts.size() : Math.min(startPost + maxPosts, sortedPosts.size())), "Posts."));
	}

	private Collection<Post> collectAllPostsForSone(Sone sone) {
		Collection<Post> allPosts = new HashSet<Post>();
		allPosts.addAll(sone.getPosts());
		allPosts.addAll(from(presentInstances(from(sone.getFriends()).transform(getCore().getDatabase().getSone()))).transformAndConcat(TO_POSTS).toList());
		allPosts.addAll(getCore().getDatabase().getDirectedPosts(sone.getId()));
		return allPosts;
	}

}
