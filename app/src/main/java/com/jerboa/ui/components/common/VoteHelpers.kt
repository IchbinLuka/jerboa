package com.jerboa.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.jerboa.R
import com.jerboa.VoteType
import com.jerboa.db.Account
import com.jerboa.getVoteString
import com.jerboa.db.entity.Account
import com.jerboa.ui.theme.muted

@Composable
fun <T> VoteGeneric(
    myVote: Int?,
    votes: Int,
    item: T,
    type: VoteType,
    onVoteClick: (item: T) -> Unit,
    showNumber: Boolean = true,
    account: Account?,
) {
    val iconAndColor = when (type) {
        VoteType.Upvote -> upvoteIconAndColor(myVote = myVote)
        else -> downvoteIconAndColor(myVote = myVote)
    }

    val votesStr = if (showNumber) {
        if (type == VoteType.Downvote && votes == 0) {
            null
        } else {
            votes.toString()
        }
    } else {
        null
    }
    val contentDescription: String = if (type == VoteType.Upvote) {
        if (myVote == 1) {
            stringResource(R.string.upvoted)
        } else {
            stringResource(R.string.upvote)
        }
    } else {
        if (myVote == 1) {
            stringResource(R.string.downvoted)
        } else {
            stringResource(R.string.downvote)
        }
    }
    ActionBarButton(
        onClick = { onVoteClick(item) },
        contentColor = iconAndColor.second,
        icon = iconAndColor.first,
        contentDescription = contentDescription,
        text = votesStr,
        account = account,
    )
}

@Composable
fun upvoteIconAndColor(myVote: Int?): Pair<ImageVector, Color> {
    return when (myVote) {
        1 -> Pair(
            ImageVector.vectorResource(id = R.drawable.upvote_filled),
            scoreColor(myVote = myVote),
        )
        else -> Pair(
            ImageVector.vectorResource(id = R.drawable.upvote_outline),
            MaterialTheme
                .colorScheme.onBackground.muted,
        )
    }
}

@Composable
fun downvoteIconAndColor(myVote: Int?): Pair<ImageVector, Color> {
    return when (myVote) {
        -1 -> Pair(
            ImageVector.vectorResource(id = R.drawable.downvote_filled),
            scoreColor(myVote = myVote),
        )
        else -> Pair(
            ImageVector.vectorResource(id = R.drawable.downvote_outline),
            MaterialTheme
                .colorScheme.onBackground.muted,
        )
    }
}


@Composable
fun VoteDisplay(
    upVotes: Int,
    downVotes: Int,
    showVotes: Boolean = true,
    myVote: Int? = null,
    account: Account?,
    onVote: (VoteType) -> Unit,
    enableDownVotes: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconColorUpvote = upvoteIconAndColor(myVote = myVote)
        val iconColorDownvote = downvoteIconAndColor(myVote = myVote)
        ActionBarButton(
            onClick = { onVote(VoteType.Upvote) },
            icon = iconColorUpvote.first,
            contentColor = iconColorUpvote.second,
            contentDescription = "",
            account = account
        )

        Text(
            text = if (showVotes) getVoteString(upVotes - downVotes) else "Vote",
            color = scoreColor(myVote = myVote),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = ButtonDefaults.IconSpacing)
        )


        if (enableDownVotes) {

            ActionBarButton(
                onClick = { onVote(VoteType.Downvote) },
                icon = iconColorDownvote.first,
                contentColor = iconColorDownvote.second,
                contentDescription = "",
                account = account
            )
        }
    }
}
