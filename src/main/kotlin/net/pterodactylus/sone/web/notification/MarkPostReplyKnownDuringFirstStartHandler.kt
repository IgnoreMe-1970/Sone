/**
 * Sone - MarkPostReplyKnownDuringFirstStartHandler.kt - Copyright © 2020 David ‘Bombe’ Roden
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

package net.pterodactylus.sone.web.notification

import com.google.common.eventbus.*
import net.pterodactylus.sone.core.event.*
import net.pterodactylus.sone.data.*
import net.pterodactylus.sone.notify.*
import net.pterodactylus.sone.utils.*
import net.pterodactylus.util.notify.*
import java.util.function.*
import javax.inject.*

/**
 * Handler that marks post replies [as known][net.pterodactylus.sone.core.Core.markReplyKnown]
 * while the [first start notification][net.pterodactylus.util.notify.NotificationManager.hasFirstStartNotification]
 * is shown.
 */
class MarkPostReplyKnownDuringFirstStartHandler @Inject constructor(private val notificationManager: NotificationManager, private val markAsKnown: Consumer<PostReply>) {

	@Subscribe
	fun newPostReply(event: NewPostReplyFoundEvent) {
		if (notificationManager.hasFirstStartNotification()) {
			markAsKnown(event.postReply)
		}
	}

}
