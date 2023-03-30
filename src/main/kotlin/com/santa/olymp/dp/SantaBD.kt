package com.santa.olymp.dp

import com.santa.olymp.dto.GroupDTO
import com.santa.olymp.dto.GroupInfoDTO
import com.santa.olymp.error.GroupNotFoundException
import com.santa.olymp.model.Group
import com.santa.olymp.model.Participant
import org.webjars.NotFoundException
import kotlin.math.abs
import kotlin.random.Random

interface SantaBD {
    fun getAllGroups(): List<Group>
    fun addGroup(newGroup: Group): Long
    fun getGroup(id: Long): Group
}

class SantaBDImpl : SantaBD {
    private val listGroups: MutableList<Group> = mutableListOf(
            Group(
                    id = 23234234,
                    name = "group Name",
                    description = "group description",
                    participants = listOf(),
            )
    )
//    = mutableListOf()

    override fun getAllGroups(): List<Group> {
        return listGroups.map {
            it.copy(
                    id = null,
                    participants = null,
            )
        }
    }

    override fun addGroup(newGroup: Group): Long {
        var nextId = abs(Random.nextLong())
        while (nextId in listGroups.mapNotNull { it.id }) {
            nextId = abs(Random.nextLong())
        }

        val newGroupIdentified = Group(
                id = nextId,
                name = newGroup.name,
                description = newGroup.description,
                participants = emptyList()
        )
        listGroups.add(newGroupIdentified)

        return nextId
    }

    override fun getGroup(id: Long): Group =
            listGroups.firstOrNull { it.id == id }?.let {
                it.copy(
                        participants = it.participants?.map { participant -> participant.excludeNestedRecipients() }
                )
            } ?: throw GroupNotFoundException("Group with passed ID: $id not found")


    private fun Participant.excludeNestedRecipients() = copy(
            recipient = this.recipient?.copy(
                    recipient = null
            )
    )

}
