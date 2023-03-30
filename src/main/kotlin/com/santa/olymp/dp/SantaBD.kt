package com.santa.olymp.dp

import com.santa.olymp.dto.GroupParamsDTO
import com.santa.olymp.dto.ParticipantInfoDTO
import com.santa.olymp.error.ElementNotFoundException
import com.santa.olymp.error.IllegalOperationException
import com.santa.olymp.error.InvalidDataException
import com.santa.olymp.error.TossNotAvailableException
import com.santa.olymp.model.Group
import com.santa.olymp.model.Participant
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs
import kotlin.random.Random

interface SantaBD {
    fun getAllGroups(): List<Group>
    fun addGroup(newGroup: Group): Long
    fun getGroup(id: Long): Group
    fun updateGroup(id: Long, newParams: GroupParamsDTO)
    fun addUserToTheGroup(groupId: Long, user: ParticipantInfoDTO): Long
    abstract fun deleteGroup(id: Long): Any
    abstract fun deleteParticipant(groupId: Long, participantId: Long)
    abstract fun tossGroup(groupId: Long): List<Participant>
    fun getRecipient(groupId: Long, participantId: Long): Participant
}

class SantaBDImpl : SantaBD {
    private val isLocked = AtomicBoolean(false)

    private val listGroups: MutableList<Group> = mutableListOf()
    private val participantIds = mutableListOf<Long>()

    override fun getAllGroups(): List<Group> {
        isLocked.set(true)
        try {
            return listGroups.map {
                it.copy(
                        participants = null,
                )
            }
        } finally {
            isLocked.set(false)
        }
    }

    override fun addGroup(newGroup: Group): Long {
        isLocked.set(true)
        try {
            val nextId = generateNextIdNotIn(listGroups.mapNotNull { it.groupId })

            println("LOGD: created groupId  = $nextId")
            val newGroupIdentified = Group(
                    groupId = nextId,
                    name = newGroup.name,
                    description = newGroup.description,
                    participants = mutableListOf()
            )
            listGroups.add(newGroupIdentified)

            return nextId
        } finally {
            isLocked.set(false)
        }
    }

//    4533039782749079600
//    4533039782749079784

    private fun generateNextIdNotIn(list: List<Long>): Long {
        isLocked.set(true)
        try {
            var nextId = abs(Random.nextLong())
            while (nextId in list) {
                nextId = abs(Random.nextLong())
            }
            return nextId
        } finally {
            isLocked.set(false)
        }
    }

    override fun getGroup(id: Long): Group {
        isLocked.set(true)
        println("LOGD: try to find: $id")
        println("LOGD: groups: $listGroups")
        try {
            return listGroups.firstOrNull { it.groupId == id }?.let {
                it.copy(
                        participants = it.participants.orEmpty().map { participant -> participant.excludeNestedRecipients() }.toMutableList()
                )
            } ?: throw ElementNotFoundException(id)
        } finally {
            isLocked.set(false)
        }
    }

    override fun updateGroup(id: Long, newParams: GroupParamsDTO) {
        isLocked.set(true)
        try {
            val group = listGroups.firstOrNull { it.groupId == id } ?: throw ElementNotFoundException(id)
            listGroups.remove(group)
            val newGroup = group.copy(
                    name = when (newParams.name) {
                        null -> group.name
                        "" -> throw IllegalOperationException("Can't remove name")
                        else -> newParams.name
                    },
                    description = when (newParams.description) {
                        null -> group.description
                        else -> newParams.description
                    }
            )

            listGroups.add(newGroup)
        } finally {
            isLocked.set(false)
        }
    }

    override fun addUserToTheGroup(groupId: Long, user: ParticipantInfoDTO): Long {
        isLocked.set(true)
        try {
            if (user.name.isNullOrEmpty()) throw InvalidDataException("Participant name cant by empty")
            val group = listGroups.firstOrNull { it.groupId == groupId } ?: throw ElementNotFoundException(groupId)

            val nextId = generateNextIdNotIn(participantIds)
            val newUser = Participant(
                    id = nextId,
                    name = user.name,
                    wish = user.wish.orEmpty(),
                    recipient = null,
            )

            group.participants?.add(newUser)

            return nextId
        } finally {
            isLocked.set(false)
        }
    }

    override fun deleteGroup(id: Long) {
        isLocked.set(true)
        try {
            val group = listGroups.firstOrNull { it.groupId == id } ?: throw ElementNotFoundException(id)

            listGroups.remove(group)
        } finally {
            isLocked.set(false)
        }
    }

    override fun deleteParticipant(groupId: Long, participantId: Long) {
        isLocked.set(true)
        try {
            val group = listGroups.firstOrNull { it.groupId == groupId } ?: throw ElementNotFoundException(groupId)
            val participant = group.participants?.firstOrNull { it.id == participantId }
                    ?: throw ElementNotFoundException(participantId, "Participant")

            group.participants.remove(participant)
        } finally {
            isLocked.set(false)
        }
    }

    override fun tossGroup(groupId: Long): List<Participant> {
        isLocked.set(true)
        try {
            val group = listGroups.firstOrNull { it.groupId == groupId } ?: throw ElementNotFoundException(groupId)

            val groupSize = group.participants?.size ?: 0
            if (groupSize < 3) {
                throw TossNotAvailableException("Toss Not available until users count not reached 3")
            } else {
                val indexesRange = (0 until groupSize).toMutableList()

                group.participants?.forEachIndexed { index, participant ->
                    val availableIndexes = indexesRange
                    availableIndexes.remove(index)

                    val recipientIndex = availableIndexes.random()

                    participant.recipient = group.participants.getOrNull(recipientIndex)

                }

                return group.participants.orEmpty().map {
                    it.excludeNestedRecipients()
                }
            }
        } finally {
            isLocked.set(false)
        }
    }

    override fun getRecipient(groupId: Long, participantId: Long): Participant {
        isLocked.set(true)
        try {
            val group = listGroups.firstOrNull { it.groupId == groupId } ?: throw ElementNotFoundException(groupId)
            val participant = group.participants?.firstOrNull { it.id == participantId }
                    ?: throw ElementNotFoundException(participantId, "Participant")

            return participant.recipient ?: throw ElementNotFoundException(participantId, "Recipient for ")
        } finally {
            isLocked.set(false)
        }
    }


    private fun Participant.excludeNestedRecipients() = copy(
            recipient = this.recipient?.copy(
                    recipient = null
            )
    )

}

